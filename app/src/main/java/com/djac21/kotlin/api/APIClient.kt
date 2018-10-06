package com.djac21.kotlin.api

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class APIClient {
    fun getClient(): Retrofit? {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://newsapi.org/v2/")
                .build()
    }
}