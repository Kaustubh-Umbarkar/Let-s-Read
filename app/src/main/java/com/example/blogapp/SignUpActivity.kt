package com.example.blogapp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.blogapp.Data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    companion object {
        val TAG = "RegisterActivity"
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth= FirebaseAuth.getInstance()
        btnSignUp.setOnClickListener {
            peformSignUp()
        }
        btnAlreadySigned.setOnClickListener {
            super.onBackPressed()
        }

    }

    private fun peformSignUp() {
        if(etEmailSignup.text.toString().isEmpty()||etFullName.text.toString().isEmpty()||
            etUserNameSignup.text.toString().isEmpty()||etPhoneNo.text.toString().isEmpty()||
            etPasswordSignup.text.toString().isEmpty())
        {
            Toast.makeText(this,"Please fill all the fields!!", Toast.LENGTH_SHORT).show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etEmailSignup.text.toString()).matches())
        {
            Toast.makeText(this,"Please enter valid email id", Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(etEmailSignup.text.toString(), etPasswordSignup.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    saveUserToDatabase()
                    goToLogin()


                } else {

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }

    }

    private fun saveUserToDatabase() {
        val ref=FirebaseDatabase.getInstance().getReference("Users")
        val uid=ref.push().key
        val user= User(uid.toString(),etUserNameSignup.text.toString(),etFullName.text.toString(),etPhoneNo.text.toString())
        if (uid != null) {
            ref.child(uid).setValue(user).addOnCompleteListener {
                Toast.makeText(this,"Data Saved Succesfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

    }
    private fun goToLogin() {
        val intent = Intent(this,Login_Activity::class.java)
        val pair1 = android.util.Pair.create<View,String>(ivChatSignup,"logo_image")
        val pair2 = android.util.Pair.create<View,String>(tvTextMainSignup,"logo_text")
        val pair3 = android.util.Pair.create<View,String>(etEmailSignup,"logo_text_email")
        val pair4 = android.util.Pair.create<View,String>(etPasswordSignup,"logo_text_password")
        val pair5 = android.util.Pair.create<View,String>(btnSignUp,"button_login")
        val pair6 = android.util.Pair.create<View,String>(btnAlreadySigned,"button_signup")


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){ val options = ActivityOptions.makeSceneTransitionAnimation(this,
            pair1,
            pair2,pair3,pair4,pair5,pair6)
            startActivity(intent,options.toBundle())}





    }



}