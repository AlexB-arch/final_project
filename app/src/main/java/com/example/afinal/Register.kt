package com.example.afinal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.afinal.data.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    // Firebase Authentication
    private var mAuth: FirebaseAuth? = null

    // Firebase Database
    private var mDatabase: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Action bar title
        supportActionBar?.title = "Register"

        // UI elements
        val username = findViewById<EditText>(R.id.register_username)
        val email = findViewById<EditText>(R.id.register_email)
        val password = findViewById<EditText>(R.id.register_password)
        val registerButton = findViewById<Button>(R.id.button_submit_register)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance()

        // Register User
        registerButton.setOnClickListener {

            // Get text from UI elements
            val usernameText = username.text.toString()
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            if (usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                mAuth!!.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                        // Initialize User
                        val user = UserEntity(emailText, usernameText)

                        // Add user to database under uid
                        val uid = mAuth!!.currentUser!!.uid
                        mDatabase!!.reference.child("users").child(uid).setValue(user)

                        // Go back to login
                        finish()
                    }
                    else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}