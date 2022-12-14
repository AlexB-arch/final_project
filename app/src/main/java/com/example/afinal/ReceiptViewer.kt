package com.example.afinal

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage


@ExperimentalGetImage class ReceiptViewer : AppCompatActivity() {

// Class variables
    private lateinit var receiptText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        // Changes the title of the action bar
        supportActionBar?.title = "Receipt"

        // UI elements
        receiptText = findViewById(R.id.receipt_text)

        // Get the text array list from the intent
        val textArray = intent.getStringArrayListExtra("textArray")
        Log.d("ReceiptViewer", "Text array: $textArray")

        // Set the text to the text view
        receiptText.text = textArray?.get(0).toString()

        // Get the image from the intent
    }
}
