package com.example.blogapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.blogapp.db.ArticleDatabase
import com.example.blogapp.repository.BlogRepository
import com.example.blogapp.ui.fragments.BlogViewModel
import com.example.blogapp.ui.fragments.BlogViewModelProviderFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_after_login.*

class AfterLogin : AppCompatActivity() {

    lateinit var viewModel:BlogViewModel
    lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login)


        val blogRepository=BlogRepository(ArticleDatabase(this))
        val viewModelProviderFactory = BlogViewModelProviderFactory(blogRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(BlogViewModel::class.java)
        bottomNavigationView.setupWithNavController(blogNavHostFragment.findNavController())


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        user= FirebaseAuth.getInstance()
        when(item.itemId){
            R.id.optionsLogOut ->{user.signOut()
            startActivity(Intent(this,Login_Activity::class.java))
            }
        }
    return true
    }
}