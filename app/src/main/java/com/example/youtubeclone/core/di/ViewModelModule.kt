package com.example.youtubeclone.core.di

import com.example.youtubeclone.ui.detail.DetailViewModel
import com.example.youtubeclone.ui.playlist.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PlaylistsViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}