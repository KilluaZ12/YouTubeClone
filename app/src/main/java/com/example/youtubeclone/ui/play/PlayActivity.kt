package com.example.youtubeclone.ui.play

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import androidx.activity.viewModels
import com.example.youtubeclone.R
import com.example.youtubeclone.core.base.BaseActivity
import com.example.youtubeclone.databinding.ActivityPlayBinding
import com.example.youtubeclone.utils.ConnectionLiveData
import com.example.youtubeclone.utils.Constants
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayActivity() : BaseActivity<ActivityPlayBinding, PlayViewModel>() {

    override fun inflateViewBinding(): ActivityPlayBinding =
        ActivityPlayBinding.inflate(layoutInflater)

    override val viewModel: PlayViewModel by viewModels()

    private var getIntentVideoId: String? = null
    private var getIntentDesc: String? = null
    private var getIntentTitle: String? = null

    private var exoPlayer: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    private fun exoPlayerInit() {
        getIntentVideoId = intent.getStringExtra(Constants.VIDEO_ID)
        exoPlayer = ExoPlayer.Builder(this)
            .build()
        binding.playerView.player = exoPlayer
        exoPlayer?.playWhenReady = true
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaItem =
            MediaItem.fromUri("https://www.dailymotion.com/video/x8ntwc5") //it doesn't work
        val mediaSource =
            DashMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
        exoPlayer?.setMediaItem(mediaItem)
        //exoPlayer.prepare()
        exoPlayer?.seekTo(currentItem, playbackPosition)
        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.playWhenReady = playWhenReady
        exoPlayer?.prepare()
    }

    private fun exoPlayerRelease() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            currentItem = player.currentMediaItemIndex
            playWhenReady = player.playWhenReady
            player.release()
        }
        exoPlayer = null
    }

    override fun onStart() {
        super.onStart()
        exoPlayerInit()
    }

    override fun onResume() {
        super.onResume()
        exoPlayerInit()
    }

    override fun onPause() {
        super.onPause()
        exoPlayerRelease()
    }

    override fun onStop() {
        super.onStop()
        exoPlayerRelease()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayerRelease()
    }

    @SuppressLint("SetTextI18n")
    override fun initLiveData() {
        super.initLiveData()
    }

    override fun initView() {
        super.initView()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        getIntentDesc = intent.getStringExtra(Constants.VIDEO_DESCRIPTION)
        getIntentTitle = intent.getStringExtra(Constants.VIDEO_TITLE)
        with(binding) {
            tvTitle.text = getIntentTitle
            tvDescr.text = getIntentDesc
        }
    }

    override fun initListener() {
        super.initListener()
        binding.llBack.setOnClickListener {
            finish()
        }
        binding.btnDownload.setOnClickListener {
            val listItems = arrayOf("1080p", "720p", "480p")
            var colorSelected = listItems[0]
            val mBuilder = AlertDialog.Builder(this)
            mBuilder.setTitle(getString(R.string.select_video_quality))
            mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
                colorSelected = listItems[i]
            }
            mBuilder.setNeutralButton(R.string.download) { dialog, _ ->
                dialog.cancel()
            }
            val mDialog = mBuilder.create()
            mDialog.show()
        }
    }

    override fun checkInternetConnection() {
        super.checkInternetConnection()
        ConnectionLiveData(application).observe(this) { isConnection ->
            if (!isConnection) {
                binding.mainContainer.visibility = View.GONE
                binding.noConnection.visibility = View.VISIBLE
            }
            binding.noInternetConnectionInclude.btnTryAgain.setOnClickListener {
                if (!isConnection) {
                    binding.mainContainer.visibility = View.GONE
                    binding.noConnection.visibility = View.VISIBLE
                } else {
                    binding.mainContainer.visibility = View.VISIBLE
                    binding.noConnection.visibility = View.GONE
                }
            }
        }
    }
}