package com.example.youtubeclone.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.youtubeclone.core.network.RemoteDataSource
import com.example.youtubeclone.core.network.Resource
import com.example.youtubeclone.data.model.PlaylistItemModel
import com.example.youtubeclone.data.model.PlaylistModel
import kotlinx.coroutines.Dispatchers

class Repository(private val remoteDataSource: RemoteDataSource) {

    fun getPlaylist(): LiveData<Resource<PlaylistModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading())
            emit(remoteDataSource.getPlaylists())
        }
    }

    fun getPlaylistItems(playlistId: String): LiveData<Resource<PlaylistItemModel>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading())
            emit(remoteDataSource.getPlaylistItems(playlistId))
        }
    }
}