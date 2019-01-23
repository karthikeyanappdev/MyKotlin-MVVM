package com.cts.myassignment.service.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cts.myassignment.service.model.Facts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProjectRepository {
    companion object {
        private var retrofit: Retrofit? = null
        val BASE_URL: String = "https://dl.dropboxusercontent.com"
        fun getApiService(): ApiService {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit!!.create(ApiService::class.java)
        }
    }

    fun getRowList(): LiveData<Facts> {
        val data = MutableLiveData<Facts>()
        getApiService().getFactList().enqueue(object : Callback<Facts> {
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
