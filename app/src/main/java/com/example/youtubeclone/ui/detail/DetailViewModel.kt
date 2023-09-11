package com.example.youtubeclone.ui.detail

import androidx.lifecycle.LiveData
import com.example.youtubeclone.core.base.BaseViewModel
import com.example.youtubeclone.core.network.Resource
import com.example.youtubeclone.data.model.PlaylistItemModel
import com.example.youtubeclone.data.repository.Repository

class DetailViewModel(private val repository: Repository) : BaseViewModel() {

    fun getPlaylistItems(playlistId: String): LiveData<Resource<PlaylistItemModel>> {
        return repository.getPlaylistItems(playlistId)
    }
}