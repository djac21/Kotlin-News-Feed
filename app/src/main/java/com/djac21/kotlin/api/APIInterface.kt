package com.djac21.kotlin.api

import com.djac21.kotlin.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("everything?sources=the-verge")
    fun getNews(@Query("apiKey") apiKey: String): Call<NewsResponse>
}