package com.cts.myassignment.service.repository

import com.cts.myassignment.service.model.Facts

import retrofit2.http.GET
import retrofit2.Call

/**
 *   API End point of URL
 */
interface ApiService {

    @GET("/s/2iodh4vg0eortkl/facts.json")
    fun getFactList(): Call<Facts>


}