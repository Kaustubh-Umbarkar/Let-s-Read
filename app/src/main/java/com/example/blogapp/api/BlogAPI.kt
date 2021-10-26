package com.example.blogapp.api

import com.example.blogapp.models.BlogResponse
import com.example.blogapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BlogAPI {
    @GET("/v2/top-headlines")
    suspend fun getNewBlog(
        @Query("country")
        countryCode: String="in",
        @Query("page")
        pageNumber:Int =1,
        @Query("apiKey")
        apiKey: String=API_KEY
    ):Response<BlogResponse>

    @GET("/v2/everything")
    suspend fun searchForBlog(
        @Query("q")
        searchQuery:String,
        @Query("page")
        pageNumber:Int =1,
        @Query("apiKey")
        apiKey: String=API_KEY
    ):Response<BlogResponse>
}