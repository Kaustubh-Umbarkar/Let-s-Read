package com.example.blogapp.repository

import com.example.blogapp.api.RetrofitInstance
import com.example.blogapp.db.ArticleDatabase
import com.example.blogapp.models.Article

class BlogRepository(val db : ArticleDatabase){
    suspend fun getNewBlog(countryCode: String, pageNumber: Int) =
            RetrofitInstance.api.getNewBlog(countryCode, pageNumber)

    suspend fun searchBlog(searchQuery: String, pageNumber: Int) =
            RetrofitInstance.api.searchForBlog(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}