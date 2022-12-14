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

package com.example.afinal.mlkit.kotlin.textdetector

import android.content.Context
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import com.example.afinal.TAG
import com.example.afinal.mlkit.GraphicOverlay
import com.example.afinal.mlkit.kotlin.VisionProcessorBase
import com.example.afinal.mlkit.preference.PreferenceUtils
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface

@ExperimentalGetImage
class TextRecognitionProcessor( private val context: Context, textRecognizerOptions: TextRecognizerOptionsInterface) : VisionProcessorBase<Text>(context) {
    private val textRecognizer: TextRecognizer = TextRecognition.getClient(textRecognizerOptions)
    private val shouldGroupRecognizedTextInBlocks: Boolean = PreferenceUtils.shouldGroupRecognizedTextInBlocks(context)
    private val showLanguageTag: Boolean = PreferenceUtils.showLanguageTag(context)
    private val showConfidence: Boolean = PreferenceUtils.shouldShowTextConfidence(context)

  override fun stop() {
    super.stop()
    textRecognizer.close()
  }

  override fun detectInImage(image: InputImage): Task<Text> {
    return textRecognizer.process(image)
  }

  public override fun onSuccess(text: Text, graphicOverlay: GraphicOverlay) {
    Log.d(TAG, "On-device Text detection successful")
    graphicOverlay.add(
      TextGraphic(
        graphicOverlay,
        text,
        shouldGroupRecognizedTextInBlocks,
        showLanguageTag,
        showConfidence
      )
    )
  }

  override fun onFailure(e: Exception) {
    Log.w(TAG, "Text detection failed.$e")
  }
}