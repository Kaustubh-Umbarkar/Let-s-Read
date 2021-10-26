package com.example.blogapp.ui.fragments

import android.app.DownloadManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.models.Article
import com.example.blogapp.models.BlogResponse
import com.example.blogapp.repository.BlogRepository
import com.example.blogapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class BlogViewModel (
        val blogRepository: BlogRepository
        ):ViewModel(){
        val newBlog:MutableLiveData<Resource<BlogResponse>> = MutableLiveData()
        var newBlogPage=1
        var blogNewResopnse:BlogResponse?=null

        val searchBlog:MutableLiveData<Resource<BlogResponse>> = MutableLiveData()
        var searchBlogPage=1
        var searchBlogResopnse:BlogResponse?=null

        init {
            getNewBlog("in")
        }

        fun getNewBlog(countryCode:String)=viewModelScope.launch {
                newBlog.postValue(Resource.Loading())
                val response=blogRepository.getNewBlog(countryCode,newBlogPage)
                newBlog.postValue(handleNewBlogResponse(response))
        }
        fun searchBlog(searchQuery:String)=viewModelScope.launch {
                searchBlog.postValue(Resource.Loading())
                val response=blogRepository.searchBlog(searchQuery,searchBlogPage)
                searchBlog.postValue(handleSearchBlogResponse(response))
        }

        private fun handleNewBlogResponse(response: Response<BlogResponse>):Resource<BlogResponse>{
                if(response.isSuccessful){
                        response.body()?.let{resultResponse->
                                newBlogPage++
                                if(blogNewResopnse== null){
                                        blogNewResopnse=resultResponse
                                }else{
                                        val oldArticles=blogNewResopnse?.articles
                                        val newArticles=resultResponse.articles
                                        oldArticles?.addAll(newArticles)
                                }
                                return Resource.Success(blogNewResopnse?:resultResponse)
                        }
                }
                return Resource.Error(response.message())
        }


        private fun handleSearchBlogResponse(response: Response<BlogResponse>):Resource<BlogResponse>{
                if(response.isSuccessful){
                        response.body()?.let{resultResponse->
                                searchBlogPage++
                                if(searchBlogResopnse== null){
                                        searchBlogResopnse=resultResponse
                                }else{
                                        val oldArticles=searchBlogResopnse?.articles
                                        val newArticles=resultResponse.articles
                                        oldArticles?.addAll(newArticles)
                                }
                                return Resource.Success(searchBlogResopnse?:resultResponse)
                        }
                }
                return Resource.Error(response.message())
        }
        fun saveArticle(article: Article) = viewModelScope.launch {
                blogRepository.upsert(article)
        }

        fun getSavedNews() = blogRepository.getSavedNews()

        fun deleteArticle(article: Article) = viewModelScope.launch {
                blogRepository.deleteArticle(article)
        }
}