package com.example.afinal

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface

/** Processor for the text detector demo. */
class TextRecognitionProcessor(
    private val context: Context,
    textRecognizerOptions: TextRecognizerOptionsInterface
) : VisionProcessorBase<Text>(context) {
    private val textRecognizer: TextRecognizer = TextRecognition.getClient(textRecognizerOptions)
    private val shouldGroupRecognizedTextInBlocks: Boolean =
        PreferenceUtils.shouldGroupRecognizedTextInBlocks(context)
    private val showLanguageTag: Boolean = PreferenceUtils.showLanguageTag(context)
    private val showConfidence: Boolean = PreferenceUtils.shouldShowTextConfidence(context)

    override fun stop() {
        super.stop()
        textRecognizer.close()
    }

    override fun detectInImage(image: InputImage): Task<Text> {
        return textRecognizer.process(image)
    }

    override fun onSuccess(text: Text, graphicOverlay: GraphicOverlay) {
        Log.d(TAG, "On-device Text detection successful")
        logExtrasForTesting(text)
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

    companion object {
        private const val TAG = "TextRecProcessor"
        private fun logExtrasForTesting(text: Text?) {
            if (text != null) {
                for (i in text.textBlocks.indices) {
                    val lines = text.textBlocks[i].lines
                    for (j in lines.indices) {
                        val elements = lines[j].elements
                        for (k in elements.indices) {
                            val element = elements[k]
                            for (point in element.cornerPoints!!) {
                                Log.d(TAG, "Text corner point: $point")
                            }
                        }
                    }
                }
            }
        }
    }
}