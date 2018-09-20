package com.mendonca.tadeu.firestoreapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
    }

    fun loginLoginClicked(view : View){
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()

        auth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception","Could not signin user: ${exception.localizedMessage}")
                }
    }

    fun loginCreateClicked(view : View){
        val createIntent = Intent(this, CreateUserActivity :: class.java)
        startActivity(createIntent)
    }
}
