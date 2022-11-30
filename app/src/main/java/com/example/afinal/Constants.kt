package com.example.afinal

const val NEW_RECEIPT_ID = 0
const val TAG = "Receipt Scanner"
const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
const val REQUEST_CODE_PERMISSIONS = 10
val REQUIRED_PERMISSIONS = mutableListOf(android.Manifest.permission.CAMERA).toTypedArray()
const val AUTOML_OBJECT_DETECTION = "Automl Object Detection"
const val AUTOML_OBJECT_DETECTION_CUSTOM = "Automl Object Detection Custom"
const val BARCODE_SCANNING = "Barcode Scanning"
const val TEXT_RECOGNITION = "Text Recognition"
const val OBJECT_DETECTION = "Object Detection"
const val OBJECT_DETECTION_CUSTOM = "Custom Object Detection"

const val STATE_SELECTED_MODEL = "selected_model"