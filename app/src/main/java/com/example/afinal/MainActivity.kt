package com.example.afinal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    // Firebase Authentication
    private var mAuth: FirebaseAuth? = null

    // Firebase Database
    private var mDatabase: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Action bar title
        supportActionBar?.title = "Home"

        // UI elements
        val welcomeText = findViewById<TextView>(R.id.test)
        val fab_newScan = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab_new_scan)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance()

        // If user is not logged in, go to Login Page
        if (mAuth!!.currentUser == null) {
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            // Get user information from Firebase Database
            val user = mAuth!!.currentUser
            val userId = user!!.uid
            val userRef = mDatabase!!.getReference("users").child(userId)
            userRef.get().addOnSuccessListener { snapshot ->
                val firstName = snapshot.child("firstName").value.toString()
                val lastName = snapshot.child("lastName").value.toString()
                welcomeText.text = "Welcome $firstName $lastName"
            }
        }

        // Go to Receipt Scanner
        fab_newScan.setOnClickListener {
            val intent = Intent(this@MainActivity, ReceiptScanner::class.java)
            startActivity(intent)
        }
    }

    // User menu inflater
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // User menu options selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Sign out user
                mAuth!!.signOut()
                // Go to Login Page
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}