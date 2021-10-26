package com.example.blogapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapp.AfterLogin
import com.example.blogapp.R
import com.example.blogapp.adapters.BlogAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_new_blog.*
import kotlinx.android.synthetic.main.fragment_saved_blog.*

class SavedBlogFragment :Fragment(R.layout.fragment_saved_blog) {

    lateinit var blogAdapter: BlogAdapter
    lateinit var viewModel:BlogViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as AfterLogin).viewModel
        setupRecyclerView()

        blogAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }

            findNavController().navigate(
                    R.id.action_savedBlogFragment_to_articleFragment,
                    bundle

            )
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = blogAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedBlog)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {articles->
            blogAdapter.differ.submitList(articles)
        })
    }

    private fun setupRecyclerView() {
        blogAdapter = BlogAdapter()
        rvSavedBlog.apply {
            adapter = blogAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}