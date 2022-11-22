package com.example.afinal

import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

typealias LumaListener = (luma: Double) -> Unit

class ReceiptScanner : AppCompatActivity() {

    // Class variables
    private var imageCapture: ImageCapture? = null

    // Firebase Functions
    private lateinit var functions: FirebaseFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_scanner)

        // Changes the title of the action bar
        supportActionBar?.title = "Receipt Scanner"

        // UI elements
        val buttonPhoto = findViewById<Button>(R.id.button_capture_photo)

        // Initialize device's camera
        ProcessCameraProvider.getInstance(this)

        // Initialize Firebase Functions
        functions = FirebaseFunctions.getInstance()

        // Get camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Add a listener to the Capture button
        buttonPhoto.setOnClickListener { takePhoto() }
    }

    // Function to start camera
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // UI element for camera preview
            val scannerPreview = findViewById<PreviewView>(R.id.scanner_viewfinder)

            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(scannerPreview.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Capture image
            imageCapture = ImageCapture.Builder().build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    // Function to take a photo
    private fun takePhoto(){
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build()

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = output.savedUri ?: Uri.fromFile(File(name))
                Toast.makeText(baseContext, "Photo saved: $savedUri", Toast.LENGTH_SHORT).show()

                // Prepare input image for Firebase Cloud Vision
                val image: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, savedUri)

                // Call Firebase Cloud Vision
                callCloudVision(image)

                // Test json output


                // Return to main activity
                finish()

                Log.d(TAG, "Photo capture succeeded: $savedUri")
            }
        })
    }

    private fun callCloudVision(image: Bitmap): Task<String> {
        return functions
            .getHttpsCallable("callCloudVision")
            .call(image)
            .continueWith { task ->
                val result = task.result?.data as String
                Log.d(TAG, result)
                result
            }
    }

    // Function to check if all permissions are granted
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    // Function to request permissions
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // Function to scale down bitmap
    private fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension
        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth =
                (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension
            resizedHeight =
                (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = maxDimension
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }

    // Function to convert bitmap to base64 string
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return encodeToString(byteArray, DEFAULT)
    }

    // Function to create a JSON request
    private fun createJsonRequest(base64: String): JsonObject {
        // Create json request to cloud vision
        val request = JsonObject()
        // Add image to request
        val image = JsonObject()
        image.add("content", JsonPrimitive(base64))
        request.add("image", image)
        //Add features to the request
        val feature = JsonObject()
        feature.add("type", JsonPrimitive("TEXT_DETECTION"))
        // Alternatively, for DOCUMENT_TEXT_DETECTION:
        // feature.add("type", JsonPrimitive("DOCUMENT_TEXT_DETECTION"))
        val features = JsonArray()
        features.add(feature)
        request.add("features", features)

        return request
    }

    // Function to call Firebase Cloud Vision
    private fun annotateImage(requestJson: JsonObject): Task<JsonElement> {
        return functions
            .getHttpsCallable("annotateImage")
            .call(requestJson)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data
                JsonParser.parseString(Gson().toJson(result))
            }
    }
}