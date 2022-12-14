package com.example.afinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import com.example.afinal.mlkit.GraphicOverlay
import com.example.afinal.mlkit.VisionImageProcessor
import com.example.afinal.mlkit.kotlin.textdetector.TextRecognitionProcessor
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@ExperimentalGetImage class StillImageActivity() : AppCompatActivity() {

    private var preview: ImageView? = null
    private var graphicOverlay: GraphicOverlay? = null

    // Max width (portrait mode)
    private var imageMaxWidth = 0
    // Max height (portrait mode)
    private var imageMaxHeight = 0
    private var imageProcessor: VisionImageProcessor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_still_image)
        findViewById<View>(R.id.select_image_button)

        preview = findViewById(R.id.preview)
        graphicOverlay = findViewById(R.id.graphic_overlay)

        // Open the image gallery
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CHOOSE_IMAGE)

        // Create the image processor. This will be used to process the images.
        val options = TextRecognizerOptions.DEFAULT_OPTIONS
        imageProcessor = TextRecognitionProcessor(this, options)

        // Set the on-click listener for the image view
        preview!!.setOnClickListener { v: View? ->
            // Open the image gallery
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CHOOSE_IMAGE)
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        createImageProcessor()
    }

    public override fun onPause() {
        super.onPause()
        imageProcessor?.run {
            this.stop()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.run {
            this.stop()
        }
    }

    private fun createImageProcessor() {
        try {
            imageProcessor = TextRecognitionProcessor(this, TextRecognizerOptions.Builder().build())

        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Can not create image processor: " + e.message, Toast.LENGTH_LONG).show()
        }
    }
}