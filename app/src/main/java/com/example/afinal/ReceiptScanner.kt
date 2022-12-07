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
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService


@ExperimentalGetImage class ReceiptScanner : AppCompatActivity() {

    // Class variables
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var buttonPhoto: Button
    private var scannerPreview: PreviewView? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var recognizer : TextRecognitionProcessor? = null
    private var analyzer : ImageAnalysis? = null
    private var processor : VisionImageProcessor? = null
    private var cameraSelector : CameraSelector? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_scanner)

        // Changes the title of the action bar
        supportActionBar?.title = "Receipt Scanner"

        // UI elements
        scannerPreview = findViewById(R.id.scanner_viewfinder)
        val buttonPhoto = findViewById<Button>(R.id.button_capture_photo)

        Log.d("ReceiptScanner", "onCreate")
        // Initialize camera provider
        ProcessCameraProvider.getInstance(this).also { providerFuture ->
            providerFuture.addListener({
                // Camera provider is now guaranteed to be available
                cameraProvider = providerFuture.get()
                Log.d("ReceiptScanner", "Camera provider initialized")
                // Bind the camera use cases
                bindCamera()
            }, ContextCompat.getMainExecutor(this))
        }



        scannerPreview = findViewById(R.id.scanner_viewfinder)
        if (scannerPreview == null) {
            Log.d("ReceiptScanner", "Preview is null")
        }

        graphicOverlay = findViewById(R.id.graphic_overlay)
        if (graphicOverlay == null) {
            Log.d("ReceiptScanner", "GraphicOverlay is null")
        }

        // Add a listener to the Capture button
        buttonPhoto.setOnClickListener { takePhoto() }
    }

    private fun bindCamera() {
        Log.d("ReceiptScanner", "Entering bindCamera()")
        if (cameraProvider != null) {
            Log.d("ReceiptScanner", "cameraProvider is not null")
            cameraProvider?.unbindAll()
            Log.d("ReceiptScanner", "cameraProvider unbound")
        }

        bindPreview()
        bindRecognizer()
    }

    private fun bindPreview() {
        Log.d("ReceiptScanner", "Entering bindPreview()")
        if (cameraProvider == null) {
            Log.d("ReceiptScanner", "cameraProvider is null")
            return
        }
        if (this.cameraProvider != null) {
            Log.d("ReceiptScanner", "cameraProvider is not null")
            cameraProvider!!.unbindAll()
            Log.d("ReceiptScanner", "Unbinding all")
        }

        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(scannerPreview?.surfaceProvider)
                Log.d("ReceiptScanner", "Preview set")
            }

        imageCapture = ImageCapture.Builder().build()
        Log.d("ReceiptScanner", "ImageCapture set")

        cameraProvider?.bindToLifecycle(
            this, CameraSelector.DEFAULT_BACK_CAMERA, preview)
        Log.d("ReceiptScanner", "preview bound to lifecycle")
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
        Log.d("ReceiptScanner", "Entering bindRecognizer()")
        if ( cameraProvider == null) {
            Log.d("ReceiptScanner", "cameraProvider is null")
            return
        }
        if ( analyzer != null) {
            Log.d("ReceiptScanner", "analyzer is not null")
            cameraProvider?.unbind(analyzer)
            Log.d("ReceiptScanner", "analyzer unbound")
        }
        if ( processor != null) {
            Log.d("ReceiptScanner", "processor is not null")
            processor?.stop()
            Log.d("ReceiptScanner", "processor stopped")
        }
        processor = TextRecognitionProcessor(this, TextRecognizerOptions.Builder().build())

        if (recognizer != null )
            Log.d("ReceiptScanner", "recognizer is not null")
        else
            Log.d("ReceiptScanner", "recognizer is null")

        analyzer = ImageAnalysis.Builder().build()
        Log.d("ReceiptScanner", "analyzer initialized")

        analyzer?.setAnalyzer( ContextCompat.getMainExecutor(this), ImageAnalysis.Analyzer { imageProxy : ImageProxy ->
            Log.d("ReceiptScanner", "Entering analyzer")
            try {
                Log.d("ReceiptScanner", "Entering try")

                if (processor == null)
                    Log.d("ReceiptScanner", "processor is null")

                processor?.processImageProxy(imageProxy, graphicOverlay)
            } catch (e: MlKitException) {
                Log.e(TAG, "Failed to process image. Error: " + e.localizedMessage)
                Toast.makeText(applicationContext, "Failed to process image. Error: " + e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
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

                    // Send the image to the next activity
                    //val intent = Intent(this@ReceiptScanner, ReceiptViewer::class.java)
                    //intent.putExtra("imageUri", savedUri)
                    //startActivity(intent)

                    finish()

                    Log.d(TAG, "Photo capture succeeded: $savedUri")
                }
            })
    }

    // Function to start camera
    /*private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(scannerPreview?.surfaceProvider)
                }

            // Image capture
            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture,
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }*/
}