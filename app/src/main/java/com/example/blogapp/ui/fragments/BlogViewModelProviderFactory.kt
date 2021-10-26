package com.example.blogapp.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blogapp.repository.BlogRepository

class BlogViewModelProviderFactory(val blogRepository:BlogRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BlogViewModel(blogRepository) as T
    }
}