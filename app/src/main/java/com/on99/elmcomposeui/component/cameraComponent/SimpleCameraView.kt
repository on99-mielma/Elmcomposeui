package com.on99.elmcomposeui.component.cameraComponent

import android.content.Context
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Composable
fun SimpleCameraView(
    modifier: Modifier = Modifier,
    preview: Preview,
    imageCapture: ImageCapture? = null,
    imageAnalysis: ImageAnalysis? = null,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    enableFlash: Boolean = false,
    focusOnTap: Boolean = true,
    flashSwitch: () -> Unit = {},
    backHandler: () -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val previewView = remember {
        PreviewView(context)
    }
    val cameraProvider by produceState<ProcessCameraProvider?>(initialValue = null) {
        value = context.getCameraProvider()
    }
    val camera = remember(cameraProvider) {
        cameraProvider?.let {
            it.unbindAll()
            it.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                *listOfNotNull(imageCapture, preview, imageAnalysis).toTypedArray()
            )
        }
    }
    LaunchedEffect(true) {
        preview.setSurfaceProvider(previewView.surfaceProvider)
        previewView.scaleType = scaleType
    }
    LaunchedEffect(camera, enableFlash) {
        camera?.let {
            if (it.cameraInfo.hasFlashUnit()) {
                it.cameraControl.enableTorch(enableFlash)
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
        }
    }
    FloatingActionButton(
        onClick = flashSwitch,
        modifier = Modifier
            .size(24.dp)
            .zIndex(20f)
            .padding(top = 700.dp, bottom = 20.dp, start = 300.dp, end = 20.dp),
        shape = CircleShape
    ) {
        if (enableFlash) {
            Icon(
                painter = painterResource(id = com.on99.elmcomposeui.R.drawable.flashlight_off),
                contentDescription = "OFF",
                modifier = Modifier.size(36.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = com.on99.elmcomposeui.R.drawable.flashlight_on),
                contentDescription = "ON",
                modifier = Modifier.size(36.dp)
            )
        }
        Log.e("modifier?", "000")
    }
    FloatingActionButton(
        onClick = backHandler,
        modifier = Modifier
            .size(24.dp)
            .zIndex(20f)
            .padding(top = 700.dp, bottom = 20.dp, start = 20.dp, end = 300.dp),
        shape = CircleShape
    ) {
        Icon(
            painter = painterResource(id = com.on99.elmcomposeui.R.drawable.arrow_back),
            contentDescription = "BACK"
        )
    }
    AndroidView(
        factory = { previewView },
        modifier = modifier
            .fillMaxSize()
            .pointerInput(camera, focusOnTap) {
                if (!focusOnTap) {
                    return@pointerInput
                }
                detectTapGestures {
                    val meteringPointFactory = SurfaceOrientedMeteringPointFactory(
                        size.width.toFloat(),
                        size.height.toFloat()
                    )
                    val meteringAction = FocusMeteringAction
                        .Builder(
                            meteringPointFactory.createPoint(it.x, it.y),
                            FocusMeteringAction.FLAG_AF
                        )
                        .disableAutoCancel()
                        .build()
                    Log.e("pointerInput", "mPF = ${meteringPointFactory}  FMA = ${meteringAction}")
                    camera?.cameraControl?.startFocusAndMetering(meteringAction)
                }
            })
}


private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))

        }
    }