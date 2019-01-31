package com.cts.myassignment.service.repository

import android.app.Application
import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 *   API fetching data by using retrofit
 */

class APIRepository(application: Application) {

    var mContext: Context? = null
    var httpCacheDirectory: File? = null

    var cache: Cache? = null
    var okHttpClient: OkHttpClient? = null
    var retrofit: Retrofit? = null
    val DEFAULT_TIMEOUT: Long = 30
    val url = "https://dl.dropboxusercontent.com"

    init {
        mContext = application.applicationContext
        if (httpCacheDirectory == null) {
            httpCacheDirectory = File(mContext?.cacheDir, "https")
        }

        if (cache == null) {
            cache = Cache(httpCacheDirectory, 1024 * 1024 * 10L)
        }


        okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .cache(cache)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .build()


        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()
    }



}