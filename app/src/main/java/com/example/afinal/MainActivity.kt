package com.example.afinal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Firebase Authentication
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI elements
        val welcomeText = findViewById<TextView>(R.id.test)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // If user is not logged in, go to Login Page
        if (mAuth!!.currentUser == null) {
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            // Welcome message
            welcomeText.text = buildString {
        append("Welcome ")
        append(mAuth!!.currentUser!!.email)
    }
        }
    }
}