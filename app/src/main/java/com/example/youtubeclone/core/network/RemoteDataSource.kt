package com.example.youtubeclone.core.network

import com.example.youtubeclone.BuildConfig
import com.example.youtubeclone.core.base.BaseDataSource
import com.example.youtubeclone.data.remote.YoutubeApiService
import com.example.youtubeclone.utils.Constants

class RemoteDataSource(private val apiService: YoutubeApiService) : BaseDataSource() {

    suspend fun getPlaylists() = getResult {
        apiService.getPlaylists(
            key = BuildConfig.API_KEY,
            part = Constants.PART,
            channelId = Constants.CHANNEL_ID,
            maxResults = 10
        )
    }

    suspend fun getPlaylistItems(playlistId: String) = getResult {
        apiService.getPlaylistItems(
            key = BuildConfig.API_KEY,
            part = Constants.PART,
            playlistId = playlistId,
            maxResults = 20
        )
    }
}