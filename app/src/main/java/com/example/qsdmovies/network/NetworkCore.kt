package com.example.qsdmovies.network

import com.example.qsdmovies.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

object NetworkCore {
    private const val API_BASE_URL = BuildConfig.API_BASE_URL
    private const val API_KEY = BuildConfig.API_KEY
    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor {
            Timber.d(it)
        }.apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                val originalHttpUrl = chain.request().url
                val url = originalHttpUrl
                    .newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()
                request.url(url)
                chain.proceed(request.build())
            })
            .build()
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val moviesAPI: MoviesAPI by lazy {
        retrofit.create(MoviesAPI::class.java)
    }
}