@file:OptIn(ExperimentalMaterial3Api::class)

package com.on99.elmcomposeui.component

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.on99.elmcomposeui.R
import com.on99.elmcomposeui.component.cameraComponent.CameraPermission
import com.on99.elmcomposeui.ui.theme.Pink40

@Deprecated("Just a test")
@Composable
fun HomeTopBarTest() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Centered TopAppBar",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeTopBar(
    shopsViewModel: ShopsViewModel,
    modifier: Modifier = Modifier
) {
//    @Deprecated(
//        message = "Use TopAppBar instead.",
//        replaceWith = ReplaceWith(
//            "TopAppBar(title, modifier, navigationIcon, actions, windowInsets, colors, " +
//                    "scrollBehavior)"
//        ),
//        level = DeprecationLevel.WARNING
//    )
//    SmallTopAppBar() {
//
//    }
    val componentDetailUiState = shopsViewModel.detailUiState.collectAsState().value
    val context = LocalContext.current
    Column(modifier = modifier) {
        TopAppBar(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                navigationIconContentColor = Pink40,
                actionIconContentColor = Pink40
            ),
            title = {},
            navigationIcon = {
                if (componentDetailUiState.isHomeTopBarNavigationButtonScreen) {
                    IconButton(onClick = {
                        Log.e(
                            "ArrowBack Button Clicked",
                            "${componentDetailUiState.isHomeTopBarNavigationButtonScreen}"
                        )
                        shopsViewModel.switchNavigationButtonScreenStateFalse()
                        shopsViewModel.switchMessageButtonScreenStateFalse()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back and Make it false"
                        )
                    }
                } else {
                    IconButton(onClick = {
                        Log.e(
                            "Menu Button Clicked",
                            "${componentDetailUiState.isHomeTopBarNavigationButtonScreen}"
                        )
                        shopsViewModel.switchMessageButtonScreenStateFalse()
                        shopsViewModel.switchNavigationButtonScreenStateTrue()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Switch and Make it true"
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = {
                    Log.e("Share Button Clicked", "I am sharing")
                    shareButtonFunction(
                        context = context,
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "To the Share"
                    )
                }
                if (componentDetailUiState.isHomeTopBarMessageButtonScreen) {
                    IconButton(onClick = {
                        Log.e("Message Button Clicked", "Check Message Box")
                        shopsViewModel.switchMessageButtonScreenStateFalse()
                        shopsViewModel.switchNavigationButtonScreenStateFalse()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = "Open the Message Box"
                        )
                    }
                } else {
                    IconButton(onClick = {
                        Log.e("Message Button Clicked", "Check Message Box")
                        shopsViewModel.switchNavigationButtonScreenStateFalse()
                        shopsViewModel.switchMessageButtonScreenStateTrue()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Message,
                            contentDescription = "Open the Message Box"
                        )
                    }
                }
            }
        )
        AnimatedVisibility(
            visible = componentDetailUiState.isHomeTopBarNavigationButtonScreen,
            enter = slideInVertically(
                initialOffsetY = { 100 }
            ) + expandVertically(
                expandFrom = Alignment.Bottom
            ) + scaleIn(
                transformOrigin = TransformOrigin(0.5f, 0f)
            ) + fadeIn(
                initialAlpha = 0.6f
            ),
            exit = slideOutVertically() + shrinkVertically() + fadeOut() + scaleOut(targetScale = 1.2f)
        ) {
            ForNavigationButtonScreen(shopsViewModel = shopsViewModel)
        }
        AnimatedVisibility(
            visible = componentDetailUiState.isHomeTopBarMessageButtonScreen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ForMessageButtonScreen(shopsViewModel = shopsViewModel)
        }
    }

}

@Composable
fun ForNavigationButtonScreen(modifier: Modifier = Modifier, shopsViewModel: ShopsViewModel) {
    BackHandler {
        shopsViewModel.switchNavigationButtonScreenStateFalse()
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "wait me draw TODO")
    }
}

@Composable
fun ForMessageButtonScreen(modifier: Modifier = Modifier, shopsViewModel: ShopsViewModel) {
    BackHandler {
        shopsViewModel.switchMessageButtonScreenStateFalse()
    }
    Box(modifier = modifier.fillMaxSize()) {
        TextBoardComponent(shopsViewModel = shopsViewModel, modifier = modifier.zIndex(-5f))
    }
}

private fun shareButtonFunction(context: Context) {
    val strlist = context.packageName
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, strlist)
    }
    val toShareIntent = Intent.createChooser(intent, null)

    try {
        startActivity(context, toShareIntent, null)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            context.getString(R.string.activity_not_found),
            Toast.LENGTH_LONG
        ).show()
        Log.e("NotFoundException", "ActivityNotFoundException")
    }
}