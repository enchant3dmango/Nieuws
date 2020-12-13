package com.example.nieuws

import com.example.nieuws.api.NewsAPI
import retrofit2.http.GET

interface APIRequest {

    @GET("/v2/top-headlines?country=id&apiKey=255e6c7f53f345179fe300cd54e597d9")
    suspend fun getNews() : NewsAPI

}