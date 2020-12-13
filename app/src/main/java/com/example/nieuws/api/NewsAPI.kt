package com.example.nieuws.api


import com.google.gson.annotations.SerializedName

data class NewsAPI(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)