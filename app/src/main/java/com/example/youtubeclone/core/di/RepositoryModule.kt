package com.example.youtubeclone.core.di

import com.example.youtubeclone.data.repository.Repository
import org.koin.dsl.module

val repositoryModule = module {
    single { Repository(get()) }
}