package com.on99.elmcomposeui.component.tensor

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.on99.elmcomposeui.component.ShopsViewModel
import org.tensorflow.lite.task.vision.detector.Detection
import java.util.LinkedList

class MyDetectorListener(
    val context: Context,
    val outsideViewModel: ShopsViewModel
) : ObjectDetectorHelper.DetectorListener {
    override fun onError(error: String) {
        Log.d(TAG, "onError: ${error}")
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun onResults(
        results: MutableList<Detection>?,
        inferenceTime: Long,
        imageHeight: Int,
        imageWidth: Int
    ) {
        outsideViewModel.setResults(
            results ?: LinkedList<Detection>(),
            inferenceTime, imageHeight, imageWidth
        )
    }

    companion object {
        val TAG = "MyDetectorListener"
    }
}