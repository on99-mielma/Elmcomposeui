package com.on99.elmcomposeui.component.componentdetail

import org.tensorflow.lite.task.vision.detector.Detection
import java.util.LinkedList
import java.util.concurrent.ExecutorService

data class OutsideUiState(
    val isCameraScreen: Boolean = false,
    val allowCameraScreenBack:Boolean = true,
    val allowCanvasShow:Boolean = false,
    val chaosForThePaint:Boolean = false,
    val tempText:String = "000",


    //tensorflow lite use
    val results:MutableList<Detection> = LinkedList<Detection>(),
    val inferenceTime: Long = -1L,
    val imageHeight: Int = -1,
    val imageWidth: Int = -1,
)
