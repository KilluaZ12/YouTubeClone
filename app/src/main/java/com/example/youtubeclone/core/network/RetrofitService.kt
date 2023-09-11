package com.example.youtubeclone.core.network

import com.example.youtubeclone.data.remote.YoutubeApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideInterceptor() }
    single { provideApi(get()) }
    factory { provideOkHttpClient(get()) }
    factory { provideRetrofit(get()) }
}

fun provideInterceptor(): Interceptor {
    return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
}

fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
    return OkHttpClient().newBuilder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(com.example.youtubeclone.BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

fun provideApi(retrofit: Retrofit): YoutubeApiService {
    return retrofit.create(YoutubeApiService::class.java)
}