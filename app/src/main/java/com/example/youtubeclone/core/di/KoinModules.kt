package com.example.youtubeclone.core.di

import com.example.youtubeclone.core.network.networkModule

val koinModules = listOf(
    repositoryModule,
    remoteDataSourceModule,
    networkModule,
    viewModelModule
)