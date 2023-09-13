package com.example.youtubeclone.ui.play

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.youtubeclone.core.base.BaseActivity
import com.example.youtubeclone.databinding.ActivityPlayBinding
import com.example.youtubeclone.databinding.AlertDialogDownloadBinding
import com.example.youtubeclone.utils.ConnectionLiveData
import com.example.youtubeclone.utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun youTubePlayerInit() = with(binding){

        getIntentVideoId = intent.getStringExtra(Constants.VIDEO_ID)
        Log.d("PlayActivity", "exoPlayerInit: $getIntentVideoId")
        lifecycle.addObserver(playerView)

        playerView.getYouTubePlayerWhenReady(object :YouTubePlayerCallback{
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadOrCueVideo(lifecycle, getIntentVideoId!!, 0f)
            }
        })
    }



    override fun onStart() {
        super.onStart()
        youTubePlayerInit()
    }

    override fun onResume() {
        super.onResume()
        youTubePlayerInit()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
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
            val binding = AlertDialogDownloadBinding.inflate(layoutInflater)
            val mBuilder = MaterialAlertDialogBuilder(this)
            mBuilder.setView(binding.root)
            /*mBuilder.setTitle(getString(R.string.select_video_quality))
            mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
                colorSelected = listItems[i]
            }
            mBuilder.setPositiveButton(R.string.download) { dialog, _ ->
                dialog.cancel()
            }*/
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