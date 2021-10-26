package com.example.blogapp

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val SPLASH_Screen:Long=3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideSystemUI()

        startApp()
        startLoginActivity()







    }
    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }


    private fun startLoginActivity() {
        Handler().postDelayed({
            val intent = Intent(this, Login_Activity::class.java)
            val pair1 = Pair.create<View,String>(ivChat,"logo_image")
            val pair2 = Pair.create<View,String>(tvTextMain,"logo_text")
            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                pair1,
                pair2)
            startActivity(intent,options.toBundle())
            finish()

        }, SPLASH_Screen)
    }

    private fun startApp() {
        val top_anim= AnimationUtils.loadAnimation(this, R.anim.top_animation)
        ivChat.startAnimation(top_anim)
        val bottom_anim= AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        tvTextMain.startAnimation(bottom_anim)
    }
}