package com.on99.elmcomposeui.component.cameraComponent

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.on99.elmcomposeui.component.ShopsViewModel
import com.on99.elmcomposeui.component.tensor.MyDetectorListener
import com.on99.elmcomposeui.component.tensor.ObjectDetectorHelper
import com.on99.elmcomposeui.component.tensor.OverlayNewView


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermission(
    multiplePermissionsState: MultiplePermissionsState,
    shopsViewModel: ShopsViewModel
) {
    BackHandler(enabled = true) {
        shopsViewModel.closeAllExtraScreen()
        shopsViewModel.switchCameraScreen()
    }
    val cameraSettingState = shopsViewModel.cameraSettingState.collectAsState().value
    val outsideUiState = shopsViewModel.outsideUiState.collectAsState().value
    val flashLightController = cameraSettingState.flashLightController
    val context = LocalContext.current
    val cameraConfigModel = remember {
        val config = AspectRatioCameraConfig(context = context)
        val model = CameraComponentViewModel(config = config)
        model
    }
    val preview = cameraConfigModel.preview
    lateinit var bitmapBuffer: Bitmap
    lateinit var objectDetectorHelper: ObjectDetectorHelper
    if (multiplePermissionsState.allPermissionsGranted) {

        objectDetectorHelper = ObjectDetectorHelper(
            context = context,
            objectDetectorListener = MyDetectorListener(
                context = context,
                outsideViewModel = shopsViewModel
            )
        )

        SimpleCameraView(
            preview = preview,
            enableFlash = flashLightController,
            imageAnalysis = cameraConfigModel.imageAnalysis.also {
                it.setAnalyzer(shopsViewModel.cameraExecutor) { image ->
                    bitmapBuffer = Bitmap.createBitmap(
                        image.width,
                        image.height,
                        Bitmap.Config.ARGB_8888
                    )
                    image.use {
                        bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer)
                    }
                    val imageRotation = image.imageInfo.rotationDegrees
                    objectDetectorHelper.detect(bitmapBuffer, imageRotation)
                }
            },
            flashSwitch = { shopsViewModel.switchFlashLight() },
            backHandler = {
                shopsViewModel.closeAllExtraScreen()
                shopsViewModel.switchCameraScreen()
            }
        )
        OverlayNewView(results = outsideUiState.results)
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(vertical = 100.dp, horizontal = 70.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = getTextShowGivenPermissions(
                        multiplePermissionsState.revokedPermissions,
                        multiplePermissionsState.shouldShowRationale
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
                    Text(text = "Request permissions")
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun getTextShowGivenPermissions(
    permissions: List<PermissionState>,
    shouldShowRationale: Boolean
): String {
    val revokedPermissionsSize = permissions.size
    if (revokedPermissionsSize == 0) {
        return ""
    }
    val textToShow = StringBuilder().apply {
        append("The ")
    }
    for (i in permissions.indices) {
        textToShow.append(permissions[i].permission)
        when {
            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
                textToShow.append(", and ")
            }

            i == revokedPermissionsSize - 1 -> {
                textToShow.append(" ")
            }

            else -> {
                textToShow.append(", ")
            }
        }
    }
    textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permission are")
    textToShow.append(
        if (shouldShowRationale) {
            " important. Please grant all of them for the app to function properly."
        } else {
            " denied. The app cannot function without them."
        }
    )
    return textToShow.toString()
}