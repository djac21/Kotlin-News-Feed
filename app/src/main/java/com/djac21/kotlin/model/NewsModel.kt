package com.djac21.kotlin.model

import com.google.gson.annotations.SerializedName

class NewsModel {
    @SerializedName("publishedAt")
    private var date: String? = null

    @SerializedName("author")
    private var author: String? = null

    @SerializedName("title")
    private var title: String? = null

    @SerializedName("description")
    private var description: String? = null

    @SerializedName("urlToImage")
    private var image: String? = null

    @SerializedName("url")
    private var url: String? = null

    fun getDate(): String? {
        return date
    }

    fun setDate(date: String) {
        this.date = date
    }

    fun getAuthor(): String? {
        return author
    }

    fun setAuthor(author: String) {
        this.author = author
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String) {
        this.image = image
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String) {
        this.url = url
    }
}