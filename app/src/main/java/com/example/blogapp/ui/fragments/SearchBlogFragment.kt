package com.example.blogapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapp.AfterLogin
import com.example.blogapp.R
import com.example.blogapp.adapters.BlogAdapter
import com.example.blogapp.util.Constants
import com.example.blogapp.util.Constants.Companion.SEARCH_BLOG_TIME_DELAY
import com.example.blogapp.util.Resource
import kotlinx.android.synthetic.main.fragment_new_blog.*
import kotlinx.android.synthetic.main.fragment_search_blog.*
import kotlinx.android.synthetic.main.fragment_search_blog.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchBlogFragment :Fragment(R.layout.fragment_search_blog)  {

    lateinit var viewModel: BlogViewModel
    lateinit var blogAdapter: BlogAdapter
    val TAG = "SearchBlogFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as AfterLogin).viewModel
        setupRecyclerView()

        blogAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }

            findNavController().navigate(
                    R.id.action_searchBlogFragment_to_articleFragment,
                    bundle

            )
        }


        var job:Job?=null
        etSearch.addTextChangedListener{editable->
            job?.cancel()
            job= MainScope().launch {
                delay(SEARCH_BLOG_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty())
                    {
                        viewModel.searchBlog(editable.toString())
                    }
                }
            }
        }


        viewModel.searchBlog.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { blogResponse ->
                        blogAdapter.differ.submitList(blogResponse.articles.toList())
                        val totalPages = blogResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchBlogPage == totalPages
                        if(isLastPage){
                            rvSearchBlog.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading=false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading=true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.searchBlog(etSearch.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        blogAdapter = BlogAdapter()
        rvSearchBlog.apply {
            adapter = blogAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchBlogFragment.scrollListener)
        }
    }
}