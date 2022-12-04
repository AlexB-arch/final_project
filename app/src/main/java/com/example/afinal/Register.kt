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
        val firstName = findViewById<EditText>(R.id.register_firstname)
        val middleName = findViewById<EditText>(R.id.register_middlename)
        val lastName = findViewById<EditText>(R.id.register_lastname)
        val phone = findViewById<EditText>(R.id.register_phone)
        val addressLine1 = findViewById<EditText>(R.id.register_address_1)
        val addressLine2 = findViewById<EditText>(R.id.register_address_2)
        val city = findViewById<EditText>(R.id.register_city)
        val state = findViewById<EditText>(R.id.register_state)
        val zip = findViewById<EditText>(R.id.register_zip)
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
            val firstNameText = firstName.text.toString()
            val middleNameText = middleName.text.toString()
            val lastNameText = lastName.text.toString()
            val addressLine1Text = addressLine1.text.toString()
            val addressLine2Text = addressLine2.text.toString()
            val cityText = city.text.toString()
            val stateText = state.text.toString()
            val zipText = zip.text.toString()
            val phoneText = phone.text.toString()

            if (usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || firstNameText.isEmpty() || lastNameText.isEmpty() || addressLine1Text.isEmpty() || cityText.isEmpty() || stateText.isEmpty() || zipText.isEmpty() || phoneText.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                mAuth!!.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                        // Initialize User
                        val user = UserEntity(firstNameText, middleNameText, lastNameText, emailText, phoneText, addressLine1Text, addressLine2Text, cityText, stateText, zipText)

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