package com.on99.elmcomposeui.component

import android.Manifest
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Scanner
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.on99.elmcomposeui.R
import com.on99.elmcomposeui.component.cameraComponent.CameraComponentViewModel
import com.on99.elmcomposeui.component.cameraComponent.CameraPermission
import com.on99.elmcomposeui.data.searchbar_ph

@Deprecated("Can not change inner padding")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarO(
    modifier: Modifier = Modifier,
    isLeftButtonOpen: Boolean = true,
    searchbarPh: searchbar_ph = searchbar_ph()
) {
    var inputText by remember {
        mutableStateOf("")
    }
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color.Blue.copy(0.5f), Color.Blue.copy(0.5f)),
        startX = 0.0f,
        endX = 100.0f,
        tileMode = TileMode.Repeated
    )
    TextField(
        value = inputText,
        onValueChange = {
            Log.e("TextField::onValueChange", "000")
            inputText = it
        },
        leadingIcon = {
            IconButton(
                onClick = { Log.e("IconButton::onClick", "Need to turn to CAREMA") },
                enabled = isLeftButtonOpen
            ) {
                Icon(
                    imageVector = Icons.Outlined.Scanner,
                    contentDescription = "CAREMAIN,Like Scan"
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.primary,
//            disabledTextColor = MaterialTheme.colorScheme.inverseSurface,
//            unfocusedSupportingTextColor = MaterialTheme.colorScheme.error,
            containerColor = Color.Transparent,
            placeholderColor = Color.DarkGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
//            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        placeholder = {
            Text(
                text = searchbarPh.getRandomOne(),
                fontSize = 15.sp,
                color = Color.DarkGray,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 0.dp, horizontal = 0.dp)
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 36.dp, max = 36.dp)
            .border(width = 3.dp, brush = gradientBrush, shape = CircleShape)
            .background(color = Color.White),
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = { Log.e("IconButton::onClick", "Need to turn to SEARCH") },
                enabled = isLeftButtonOpen
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search"
                )
            }
        }
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SearchBar1(
    modifier: Modifier = Modifier,
    isLeftButtonOpen: Boolean = true,
    searchbarPh: searchbar_ph = searchbar_ph(),
    stateViewModel: ShopsViewModel
) {

    //val componentDetailUiState = stateViewModel.detailUiState.collectAsState().value
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color.Blue.copy(0.5f), Color.Blue.copy(0.5f)),
        startX = 0.0f,
        endX = 100.0f,
        tileMode = TileMode.Repeated
    )
    CustomTextField(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 36.dp, max = 36.dp)
            .border(width = 3.dp, brush = gradientBrush, shape = CircleShape)
            .background(color = Color.White),
        showCleanIcon = true,
        searchbarPh = searchbarPh,
        leadingIcon = {
            IconButton(
                onClick = {
                    Log.e("IconButton::onClick", "Need to turn to CAREMA")
                    stateViewModel.switchCameraScreenTrue()
                },
                enabled = isLeftButtonOpen
            ) {
                Icon(
                    imageVector = Icons.Outlined.QrCode,
                    contentDescription = "CAREMAIN,Like Scan"
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { Log.e("IconButton::onClick", "Need to turn to SEARCH") },
                enabled = isLeftButtonOpen,
//                modifier = Modifier.background(Color.Blue).size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search"
                )
            }
        }
    )
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    searchbarPh: searchbar_ph = searchbar_ph(),
    showCleanIcon: Boolean = false,
    onTextChange: String.() -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: String.() -> Unit = {},
    //textFieldStyle: TextStyle = LocalTextStyle.current,
    hintTextStyle: TextStyle = LocalTextStyle.current
) {
    var text by remember {
        mutableStateOf("")
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.invoke()
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onTextChange.invoke(it)
            },
            cursorBrush = SolidColor(Color.Black),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            decorationBox = { innerTextField ->
                if (text.isBlank() && searchbarPh.size() > 0)
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                        Text(
                            text = searchbarPh.getRandomOne(),
                            modifier = Modifier.fillMaxWidth(),
                            style = hintTextStyle,
                            color = Color.LightGray
                        )
                    } else innerTextField()
            },
            keyboardActions = KeyboardActions {
                keyboardActions(text)
            },
            keyboardOptions = keyboardOptions
        )
        if (!text.isBlank())
            IconButton(onClick = { text = "" }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_cancel_24),
                    contentDescription = null
                )
            }
        trailingIcon?.invoke()
    }
}
