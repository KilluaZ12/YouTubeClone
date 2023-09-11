package com.example.youtubeclone.ui.playlist

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels

import com.example.youtubeclone.core.base.BaseActivity
import com.example.youtubeclone.core.network.Resource
import com.example.youtubeclone.data.model.PlaylistModel
import com.example.youtubeclone.databinding.ActivityPlaylistsBinding
import com.example.youtubeclone.ui.detail.DetailActivity
import com.example.youtubeclone.utils.ConnectionLiveData
import com.example.youtubeclone.utils.Constants

class PlaylistsActivity : BaseActivity<ActivityPlaylistsBinding, PlaylistsViewModel>() {

    override fun inflateViewBinding(): ActivityPlaylistsBinding =
        ActivityPlaylistsBinding.inflate(layoutInflater)

    override val viewModel: PlaylistsViewModel by viewModels()

    private val adapter = PlaylistsAdapter(this::onClick)

    override fun initLiveData() {
        super.initLiveData()
        viewModel.getPlayList().observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    Toast.makeText(this, "success status", Toast.LENGTH_SHORT).show()
                    adapter.setList(response.data?.items)
                    viewModel.loading.value = false
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
        }
        viewModel.loading.observe(this) { status ->
            if (status) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }
    }

    override fun checkInternetConnection() {
        super.checkInternetConnection()
        ConnectionLiveData(application).observe(this) { isConnection ->
            if (!isConnection) {
                binding.noConnection.visibility = View.VISIBLE
                binding.mainContainer.visibility = View.GONE
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

    private fun onClick(item: PlaylistModel.Item) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Constants.PLAYLIST_ID, item.id)
        intent.putExtra(Constants.PLAYLIST_TITLE, item.snippet.title)
        intent.putExtra(Constants.PLAYLIST_DESCRIPTION, item.snippet.description)
        intent.putExtra(Constants.VIDEO_ITEM_COUNT, item.contentDetails.itemCount.toString())
        startActivity(intent)
    }

    override fun initView() {
        super.initView()
        binding.recyclerView.adapter = adapter
    }
}