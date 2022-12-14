package com.example.afinal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@ExperimentalGetImage class MainActivity : AppCompatActivity() {

    // UI elements
    private lateinit var greetingText: TextView
    private lateinit var fabNewTrip: FloatingActionButton
    private lateinit var choosePic : Button

    // Firebase Authentication
    private var mAuth: FirebaseAuth? = null

    // Firebase Database
    private var mDatabase: FirebaseDatabase? = null

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use Jetpack Navigation to navigate to the MainFragment
        setContentView(R.layout.activity_main)

        // Action bar title
        supportActionBar?.title = "Home"

        // UI elements
        greetingText = findViewById(R.id.greeting_text)
        fabNewTrip = findViewById(R.id.button_new_trip)
        //choosePic = findViewById(R.id.choose_picture_button)

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
            val user = mAuth!!.currentUser?.uid
            val userRef = mDatabase!!.getReference("users").child(user!!)
            userRef.get().addOnSuccessListener { snapshot ->
                val username = snapshot.child("username").value.toString()
                greetingText.text = "Welcome, $username"
            }
        }

        if (!allRuntimePermissionsGranted()) {
            getRuntimePermissions()
        }

        // Go to Receipt Scanner
        fabNewTrip.setOnClickListener {
            val intent = Intent(this@MainActivity, NewTrip::class.java)
            startActivity(intent)
        }
        //choosePic.setOnClickListener {
            //val intent = Intent(Intent.ACTION_PICK)
            //intent.type = "image/*"
        //}

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
                // Go back to Login Page
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun allRuntimePermissionsGranted(): Boolean {
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val permissionsToRequest = ArrayList<String>()
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    permissionsToRequest.add(permission)
                }
            }
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Permission granted: $permission")
            return true
        }
        Log.i(TAG, "Permission NOT granted: $permission")
        return false
    }
}