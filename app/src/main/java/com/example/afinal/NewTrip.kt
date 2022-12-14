package com.example.afinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NewTrip : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip)

        // Hide action bar
        supportActionBar?.hide()
    }
}