package com.example.youtubeclone.ui.playlist

import androidx.lifecycle.LiveData
import com.example.youtubeclone.core.base.BaseViewModel
import com.example.youtubeclone.core.network.Resource
import com.example.youtubeclone.data.model.PlaylistModel
import com.example.youtubeclone.data.repository.Repository

class PlaylistsViewModel(private val repository: Repository) : BaseViewModel() {

    fun getPlayList(): LiveData<Resource<PlaylistModel>> {
        return repository.getPlaylist()
    }
}