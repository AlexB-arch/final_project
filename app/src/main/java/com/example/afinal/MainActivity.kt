package com.example.afinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.afinal.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Firebase Authentication
    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // if user is not logged in, go to login page, else go to main page
        if (mAuth!!.currentUser == null) {
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}