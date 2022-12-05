package com.example.afinal

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


@ExperimentalGetImage class ReceiptViewer : AppCompatActivity() {

// Class variables
    private lateinit var receiptText: TextView
    private lateinit var receiptImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        // Changes the title of the action bar
        supportActionBar?.title = "Receipt"

        // UI elements
        receiptText = findViewById(R.id.receipt_text)
        receiptImage = findViewById(R.id.receipt_view)

        // Get the image from the previous activity
        val imageUri = intent.getParcelableExtra<Uri>("imageUri")

        // Set the image to the ImageView
        receiptImage.setImageURI(imageUri)

        // Make the image into a bitmap
        val image = InputImage.fromFilePath(this, imageUri!!)
        val bitmap = image.bitmapInternal


    }

    fun analyze(bitmap: Bitmap) {

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        val image = InputImage.fromBitmap(bitmap, 0)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                for (block in visionText.textBlocks) {
                    val blockText = block.text
                    val blockCornerPoints = block.cornerPoints
                    val blockFrame = block.boundingBox
                    for (line in block.lines) {
                        val lineText = line.text
                        val lineCornerPoints = line.cornerPoints
                        val lineFrame = line.boundingBox
                        for (element in line.elements) {
                            val elementText = element.text
                            val elementCornerPoints = element.cornerPoints
                            val elementFrame = element.boundingBox
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                Log.d("Error", "Error: ${e.message}")
                }
            }
}
