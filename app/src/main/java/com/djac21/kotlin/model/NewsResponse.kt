package com.djac21.kotlin.model

import com.djac21.kotlin.model.NewsModel
import com.google.gson.annotations.SerializedName

class NewsResponse {
    @SerializedName("articles")
    private var results: List<NewsModel>? = null

    fun getResults(): List<NewsModel>? {
        return results
    }

    fun setResults(results: List<NewsModel>) {
        this.results = results
    }
}