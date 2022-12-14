package com.example.afinal

import android.Manifest

const val NEW_RECEIPT_ID = 0
const val TAG = "ReceiptScanner"
const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
const val REQUEST_CODE_PERMISSIONS = 10
const val PERMISSION_REQUESTS = 1
private const val TEXT_RECOGNITION_LATIN = "Text Recognition Latin"
const val SIZE_SCREEN = "w:screen" // Match screen width
private const val SIZE_1024_768 = "w:1024" // ~1024*768 in a normal ratio
private const val SIZE_640_480 = "w:640" // ~640*480 in a normal ratio
private const val SIZE_ORIGINAL = "w:original" // Original image size
private const val REQUEST_IMAGE_CAPTURE = 1001
const val REQUEST_CHOOSE_IMAGE = 1002

val REQUIRED_RUNTIME_PERMISSIONS =
    arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )