package com.example.youtubeclone.ui.play

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import com.example.youtubeclone.R
import com.example.youtubeclone.core.base.BaseActivity
import com.example.youtubeclone.databinding.ActivityPlayBinding
import com.example.youtubeclone.utils.ConnectionLiveData
import com.example.youtubeclone.utils.Constants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayActivity() : BaseActivity<ActivityPlayBinding, PlayViewModel>() {

    override fun inflateViewBinding(): ActivityPlayBinding =
        ActivityPlayBinding.inflate(layoutInflater)

    override val viewModel: PlayViewModel by viewModel()

    private var getIntentVideoId: String? = null
    private var getIntentDesc: String? = null
    private var getIntentTitle: String? = null

    private var exoPlayer: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun exoPlayerInit() = with(binding){

        getIntentVideoId = intent.getStringExtra(Constants.VIDEO_ID)
        Log.d("PlayActivity", "exoPlayerInit: $getIntentVideoId")
        lifecycle.addObserver(playerView)

        playerView.getYouTubePlayerWhenReady(object :YouTubePlayerCallback{
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadOrCueVideo(lifecycle, getIntentVideoId!!, 0f)
            }
        })
        //exoPlayer?.playWhenReady = true

         //it doesn't work
        val mediaItem = MediaItem.fromUri("https://www.youtube.com/watch?v=yAk2fgFRYDc")
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaSource = DashMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
        //exoPlayer?.seekTo(currentItem, playbackPosition)
        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.playWhenReady = true
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