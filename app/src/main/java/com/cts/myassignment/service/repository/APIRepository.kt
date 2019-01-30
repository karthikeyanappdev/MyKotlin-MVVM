package com.cts.myassignment.service.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.cts.myassignment.service.model.Facts
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    companion object {
        @Volatile
        var instance: APIRepository? = null

        fun getInstance(application: Application): APIRepository {
            if (instance == null) {
                synchronized(APIRepository::class) {
                    if (instance == null) {
                        instance = APIRepository(application)
                    }
                }
            }
            return instance!!
        }


    }

    fun getRowList(application: Application): LiveData<Facts> {
        val data = MutableLiveData<Facts>()
        getInstance(application).retrofit?.create(ApiService::class.java)?.getFactList()
            ?.enqueue(object : Callback<Facts> {
                override fun onResponse(call: Call<Facts>, response: Response<Facts>) {
                    data.setValue(response.body())
                }

                override fun onFailure(call: Call<Facts>, t: Throwable) {
                    data.setValue(null)
                }
            })
        return data
    }

}