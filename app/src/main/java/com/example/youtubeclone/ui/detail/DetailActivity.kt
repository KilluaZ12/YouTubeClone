package com.example.youtubeclone.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.example.youtubeclone.core.base.BaseActivity
import com.example.youtubeclone.core.network.Resource
import com.example.youtubeclone.data.model.PlaylistItemModel
import com.example.youtubeclone.databinding.ActivityDetailBinding
import com.example.youtubeclone.ui.play.PlayActivity
import com.example.youtubeclone.utils.ConnectionLiveData
import com.example.youtubeclone.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity() : BaseActivity<ActivityDetailBinding, DetailViewModel>() {

    override fun inflateViewBinding(): ActivityDetailBinding =
        ActivityDetailBinding.inflate(layoutInflater)

    override val viewModel: DetailViewModel by viewModel()


    private val adapter = DetailAdapter(this::onClick)

    private fun onClick(item: PlaylistItemModel.Item) {
        val intent = Intent(this, PlayActivity::class.java)
        intent.putExtra(Constants.VIDEO_ID, item.contentDetails?.videoId)
        intent.putExtra(Constants.VIDEO_TITLE, item.snippet?.title)
        intent.putExtra(Constants.VIDEO_DESCRIPTION, item.snippet?.description)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    override fun initLiveData() {
        super.initLiveData()
        val getIntentId = intent.getStringExtra(Constants.PLAYLIST_ID)
        val getIntentDesc = intent.getStringExtra(Constants.PLAYLIST_DESCRIPTION)
        val getIntentTitle = intent.getStringExtra(Constants.PLAYLIST_TITLE)
        val getIntentItemCount = intent.getStringExtra(Constants.VIDEO_ITEM_COUNT)

        viewModel.getPlaylistItems(getIntentId!!).observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    Toast.makeText(this, "success status", Toast.LENGTH_SHORT).show()
                    adapter.setList(response.data?.items as List<PlaylistItemModel.Item>?)
                    viewModel.loading.value = false
                    binding.tvTitle.text = getIntentTitle
                    binding.tvDesc.text = getIntentDesc
                    binding.tvNumberOfSeries.text = "$getIntentItemCount video series"
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this, "error status", Toast.LENGTH_SHORT).show()
                    viewModel.loading.value = false
                }

                Resource.Status.LOADING -> {
                    Toast.makeText(this, "loading status", Toast.LENGTH_SHORT).show()
                    viewModel.loading.value = true
                }
            }
            viewModel.loading.observe(this) { status ->
                if (status) binding.progressBar.visibility = View.VISIBLE
                else binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun initView() {
        super.initView()
        binding.includeRecyclerView.recyclerView.adapter = adapter
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun initListener() {
        super.initListener()
        binding.llBack.setOnClickListener {
            finish()
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