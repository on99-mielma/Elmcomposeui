package com.on99.elmcomposeui.component.cameraComponent

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CameraComponentViewModel(config: CameraConfig) : ViewModel() {
    val preview = config.options(Preview.Builder())
    val imageCapture: ImageCapture = config.options(ImageCapture.Builder())
    val imageAnalysis: ImageAnalysis = config.options(ImageAnalysis.Builder())
}