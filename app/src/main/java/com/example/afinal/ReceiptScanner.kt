package com.example.afinal

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.afinal.mlkit.GraphicOverlay
import com.example.afinal.mlkit.VisionImageProcessor
import com.example.afinal.mlkit.kotlin.textdetector.TextRecognitionProcessor
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalGetImage class ReceiptScanner : AppCompatActivity() {

    // Class variables
    private var textRecognizer : TextRecognition? = null
    private var imageCapture: ImageCapture? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var buttonScan: Button ?= null
    private var scannerPreview: PreviewView? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var analyzer : ImageAnalysis? = null
    private var processor : VisionImageProcessor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_scanner)

        // Changes the title of the action bar
        supportActionBar?.title = "Receipt Scanner"

        // UI elements
        scannerPreview = findViewById(R.id.scanner_viewfinder)
        graphicOverlay = findViewById(R.id.graphic_overlay)
        buttonScan = findViewById(R.id.button_scan)

        // Initialize camera provider
        ProcessCameraProvider.getInstance(this).also { providerFuture ->
            providerFuture.addListener({
                // Camera provider is now guaranteed to be available
                cameraProvider = providerFuture.get()
                // Bind the camera use cases
                bindCamera()
            }, ContextCompat.getMainExecutor(this))
        }

        // Add a listener to the Capture button
        buttonScan?.setOnClickListener {
            takePhoto()
        }
    }

    private fun bindCamera() {
        if (cameraProvider != null) {
            cameraProvider?.unbindAll()
        }

        bindPreview()
        bindRecognizer()
    }

    private fun bindPreview() {
        if (cameraProvider == null) {
            return
        }
        if (this.cameraProvider != null) {
            cameraProvider!!.unbindAll()
        }

        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(scannerPreview?.surfaceProvider)
            }

        imageCapture = ImageCapture.Builder().build()

        cameraProvider?.bindToLifecycle(
            this, CameraSelector.DEFAULT_BACK_CAMERA, preview)
    }

    public override fun onResume() {
        super.onResume()
        bindCamera()
    }

    override fun onPause() {
        super.onPause()

        processor?.run { this.stop() }
    }

    // Function to stop camera executor
    override fun onDestroy() {
        super.onDestroy()
        processor?.run { this.stop() }
    }

    private fun bindRecognizer(){
        if ( cameraProvider == null) {
            return
        }
        if ( analyzer != null) {
            cameraProvider?.unbind(analyzer)
        }
        if ( processor != null) {
            processor?.stop()
        }
        processor = TextRecognitionProcessor(this, TextRecognizerOptions.Builder().build())
        //textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        analyzer = ImageAnalysis.Builder().build()

        analyzer?.setAnalyzer( ContextCompat.getMainExecutor(this)) { imageProxy: ImageProxy ->
            try {
                processor?.processImageProxy(imageProxy, graphicOverlay)

            } catch (e: MlKitException) {
                Toast.makeText(
                    applicationContext,
                    "Failed to process image. Error: " + e.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
        cameraProvider!!.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, analyzer)
    }

    // Function to take a photo
    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(File(name))
                    Toast.makeText(baseContext, "Photo saved: $savedUri", Toast.LENGTH_SHORT).show()

                    finish()

                    Log.d(TAG, "Photo capture succeeded: $savedUri")
                }
            })
    }
}