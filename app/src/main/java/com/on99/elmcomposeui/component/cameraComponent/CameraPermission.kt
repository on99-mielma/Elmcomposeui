package com.on99.elmcomposeui.component.cameraComponent

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


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermission(
    multiplePermissionsState: MultiplePermissionsState,
    shopsViewModel: ShopsViewModel
) {
    BackHandler(enabled = true) {
        shopsViewModel.closeAllExtraScreen()
    }
    val cameraSettingState = shopsViewModel.cameraSettingState.collectAsState().value
    val flashLightController = cameraSettingState.flashLightController
    val context = LocalContext.current
    val cameraConfigModel = remember {
        val config = AspectRatioCameraConfig(context = context)
        val model = CameraComponentViewModel(config = config)
        model
    }
    val preview = cameraConfigModel.preview
    if (multiplePermissionsState.allPermissionsGranted) {
        SimpleCameraView(
            preview = preview,
            enableFlash = flashLightController,
            flashSwitch = { shopsViewModel.switchFlashLight() },
            backHandler = {
                shopsViewModel.closeAllExtraScreen()
            }
        )
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