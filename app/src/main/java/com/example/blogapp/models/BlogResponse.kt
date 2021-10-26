package com.example.blogapp.models

import com.example.blogapp.models.Article

data class BlogResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)