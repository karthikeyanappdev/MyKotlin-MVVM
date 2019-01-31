package com.cts.myassignment.service.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cts.myassignment.service.`object`.AssignmentApplication
import com.cts.myassignment.service.model.Facts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiFetch(private var application: Application) {


    fun getRowList(): LiveData<Facts> {
        val data = MutableLiveData<Facts>()
        AssignmentApplication.getInstance(application).retrofit?.create(ApiService::class.java)?.getFactList()
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