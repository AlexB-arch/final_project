package com.example.afinal

import android.Manifest

const val NEW_RECEIPT_ID = 0
const val TAG = "ReceiptScanner"
const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
const val REQUEST_CODE_PERMISSIONS = 10
const val PERMISSION_REQUESTS = 1

val REQUIRED_RUNTIME_PERMISSIONS =
    arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )