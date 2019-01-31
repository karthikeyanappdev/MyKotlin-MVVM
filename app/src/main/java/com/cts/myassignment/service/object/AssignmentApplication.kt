package com.cts.myassignment.service.`object`

import android.app.Application
import com.cts.myassignment.service.repository.APIRepository
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import com.jakewharton.picasso.OkHttp3Downloader



class AssignmentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        getInstance(this)
        picassoBuilder(this)
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

    fun picassoBuilder(application: Application){
        var builder :Picasso.Builder = Picasso.Builder(application)
        builder.downloader(OkHttp3Downloader(this, Integer.MAX_VALUE.toLong()))
        val built = builder.build()
        built.setIndicatorsEnabled(true)
        built.setLoggingEnabled(true)
        Picasso.setSingletonInstance(built)

    }
}