package com.example.blogapp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login_.*

class Login_Activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_)
        auth= FirebaseAuth.getInstance()
        btnNewUser.setOnClickListener {
            goToSignUp()
        }
        btnLogIn.setOnClickListener {
            performLogIn()
        }

    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser!=null)
        {
            val intent= Intent(this,AfterLogin::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun performLogIn() {
        if(etEmailLogin.text.toString().isEmpty()||
            etPasswordLogin.text.toString().isEmpty())
        {
            Toast.makeText(this,"Please fill all the fields!!", Toast.LENGTH_SHORT).show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etEmailLogin.text.toString()).matches())
        {
            Toast.makeText(this,"Please enter valid email id", Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(etEmailLogin.text.toString(), etPasswordLogin.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information


                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                    // ...
                }

                // ...
            }
    }

    private fun goToSignUp() {
        val intent = Intent(this,SignUpActivity::class.java)
        val pair1 = android.util.Pair.create<View,String>(ivLoginLogo,"logo_image")
        val pair2 = android.util.Pair.create<View,String>(tvTextMainSignup,"logo_text")
        val pair3 = android.util.Pair.create<View,String>(etEmailLogin,"logo_text_email")
        val pair4 = android.util.Pair.create<View,String>(etPasswordLogin,"logo_text_password")
        val pair5 = android.util.Pair.create<View,String>(btnLogIn,"button_login")
        val pair6 = android.util.Pair.create<View,String>(btnNewUser,"button_signup")


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){ val options = ActivityOptions.makeSceneTransitionAnimation(this,
            pair1,
            pair2,pair3,pair4,pair5,pair6)
            startActivity(intent,options.toBundle())}





    }

}