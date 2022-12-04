package com.example.afinal

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class ReceiptViewer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        // Changes the title of the action bar
        supportActionBar?.title = "Receipt"

        // UI elements
        val receiptText = findViewById<TextView>(R.id.display_receipt)

        // Get the text from the intent
        val text = intent.extras?.getString("text")

        // Display the text
        receiptText.text = text

    }
}