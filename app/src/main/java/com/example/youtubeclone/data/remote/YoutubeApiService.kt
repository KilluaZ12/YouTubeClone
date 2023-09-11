package com.example.youtubeclone.data.remote

import com.example.youtubeclone.data.model.PlaylistItemModel
import com.example.youtubeclone.data.model.PlaylistModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {
    @GET("playlists")
    suspend fun getPlaylists(
        @Query("part") part: String,
        @Query("channelId") channelId: String,
        @Query("key") key: String,
        @Query("maxResults") maxResults: Int,
    ): Response<PlaylistModel>

    @GET("playlistItems")
    suspend fun getPlaylistItems(
        @Query("part") part: String,
        @Query("playlistId") playlistId: String,
        @Query("key") key: String,
        @Query("maxResults") maxResults: Int
    ): Response<PlaylistItemModel>
}