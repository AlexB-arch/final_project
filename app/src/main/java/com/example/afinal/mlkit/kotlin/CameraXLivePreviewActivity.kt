/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.afinal.mlkit.kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.afinal.R
import com.example.afinal.mlkit.GraphicOverlay
import com.example.afinal.mlkit.VisionImageProcessor
import com.example.afinal.mlkit.kotlin.textdetector.TextRecognitionProcessor
import com.example.afinal.mlkit.preference.PreferenceUtils
import com.google.android.gms.common.annotation.KeepName
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@ExperimentalGetImage /** Live preview demo app for ML Kit APIs using CameraX. */
@KeepName
class CameraXLivePreviewActivity :
  AppCompatActivity()  {

  private var previewView: PreviewView? = null
  private var graphicOverlay: GraphicOverlay? = null
  private var cameraProvider: ProcessCameraProvider? = null
  private var previewUseCase: Preview? = null
  private var analysisUseCase: ImageAnalysis? = null
  private var imageProcessor: VisionImageProcessor? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d(TAG, "onCreate")
    setContentView(R.layout.activity_vision_camerax_live_preview)
    previewView = findViewById(R.id.preview_view)
    if (previewView == null) {
      Log.d(TAG, "previewView is null")
    }
    graphicOverlay = findViewById(R.id.graphic_overlay)
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null")
    }
  }

  public override fun onResume() {
    super.onResume()
    bindAllCameraUseCases()
  }

  override fun onPause() {
    super.onPause()

    imageProcessor?.run { this.stop() }
  }

  public override fun onDestroy() {
    super.onDestroy()
    imageProcessor?.run { this.stop() }
  }

  private fun bindAllCameraUseCases() {
    if (cameraProvider != null) {
      // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
      cameraProvider!!.unbindAll()
      bindPreviewUseCase()
      bindAnalysisUseCase()
    }
  }

  private fun bindPreviewUseCase() {
    if (!PreferenceUtils.isCameraLiveViewportEnabled(this)) {
      return
    }
    if (cameraProvider == null) {
      return
    }
    if (previewUseCase != null) {
      cameraProvider!!.unbind(previewUseCase)
    }

    val builder = Preview.Builder()

    previewUseCase = builder.build()
    previewUseCase!!.setSurfaceProvider(previewView!!.surfaceProvider)
    cameraProvider!!.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, previewUseCase)
  }

  private fun bindAnalysisUseCase() {
    if (cameraProvider == null) {
      return
    }
    if (analysisUseCase != null) {
      cameraProvider!!.unbind(analysisUseCase)
    }
    if (imageProcessor != null) {
      imageProcessor!!.stop()
    }
    TextRecognitionProcessor(this, TextRecognizerOptions.Builder().build())

    val builder = ImageAnalysis.Builder()
    analysisUseCase = builder.build()
    analysisUseCase?.setAnalyzer(
      // imageProcessor.processImageProxy will use another thread to run the detection underneath,
      // thus we can just runs the analyzer itself on main thread.
      ContextCompat.getMainExecutor(this)
    ) { imageProxy: ImageProxy ->
      try {
        imageProcessor!!.processImageProxy(imageProxy, graphicOverlay)
      } catch (e: MlKitException) {
        Log.e(TAG, "Failed to process image. Error: " + e.localizedMessage)
        Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
      }
    }
    cameraProvider!!.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, analysisUseCase)
  }

  companion object {
    private const val TAG = "CameraXLivePreview"
  }
}
