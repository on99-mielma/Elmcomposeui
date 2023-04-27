package com.on99.elmcomposeui.component.cameraComponent

import android.content.Context
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min

open class CameraConfig {
    open fun options(builder: Preview.Builder): Preview {
        return builder.build()
    }

    open fun options(builder: CameraSelector.Builder): CameraSelector {
        return builder.build()
    }

    open fun options(builder: ImageCapture.Builder): ImageCapture {
        return builder.build()
    }

    open fun options(builder: ImageAnalysis.Builder): ImageAnalysis {
        return builder
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
    }

}

class AspectRatioCameraConfig(context: Context) : CameraConfig() {
    private val mAspectRatio: Int


    private fun aspectRatio(width: Float, height: Float): Int {
        val ratio = Math.max(width, height) / Math.min(width, height)
        return if (Math.abs(ratio - 4.0f / 3.0f) < Math.abs(ratio - 16.0f / 9.0f)) {
            AspectRatio.RATIO_4_3
        } else AspectRatio.RATIO_16_9
    }


    private val RATIO_4_3_VALUE = 4.0 / 3.0
    private val RATIO_16_9_VALUE = 16.0 / 9.0


    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = ln(max(width, height).toDouble() / min(width, height))
        if (abs(previewRatio - ln(RATIO_4_3_VALUE))
            <= abs(previewRatio - ln(RATIO_16_9_VALUE))
        ) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    override fun options(builder: Preview.Builder): Preview {
        return super.options(builder)
    }

    override fun options(builder: CameraSelector.Builder): CameraSelector {
        return super.options(builder)
    }

    override fun options(builder: ImageAnalysis.Builder): ImageAnalysis {
        builder.setTargetAspectRatio(mAspectRatio)
        return super.options(builder)
    }

    override fun options(builder: ImageCapture.Builder): ImageCapture {
        builder.setTargetAspectRatio(mAspectRatio)
        return super.options(builder)
    }

    init {
        val displayMetrics = context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        mAspectRatio = aspectRatio(width.toFloat(), height.toFloat())
        Log.d("CameraConfig", "aspectRatio:$mAspectRatio")
    }
}