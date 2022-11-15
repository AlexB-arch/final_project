package com.example.afinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    // Firebase Authentication
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // UI elements

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
    }
}