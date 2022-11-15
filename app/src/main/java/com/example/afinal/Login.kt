package com.example.afinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    // Firebase Authentication
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.login_email)
        val password = findViewById<EditText>(R.id.login_password)
        val loginButton = findViewById<Button>(R.id.button_login)
        val registerButton = findViewById<Button>(R.id.button_register)

        loginButton.setOnClickListener { v: View? ->
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this@Login, "Please enter all fields", Toast.LENGTH_SHORT).show()
            } else {
                mAuth!!.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener { task: Task<AuthResult?> ->
                        if (task.isSuccessful) {
                            val user = mAuth!!.currentUser
                            updateUI(user)
                        } else {
                            Toast.makeText(this@Login, "Login failed", Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
            }
        }

        registerButton.setOnClickListener { v: View? ->
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }
    }

    private fun updateUI(o: Any?) {
        val intent = Intent(this@Login, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }
}