package com.example.youtubeclone.core.di

import com.example.youtubeclone.core.network.RemoteDataSource
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single { RemoteDataSource(get()) }
}