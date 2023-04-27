package com.on99.elmcomposeui

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.on99.elmcomposeui.component.*
import com.on99.elmcomposeui.component.cameraComponent.CameraComponentViewModel
import com.on99.elmcomposeui.component.cameraComponent.CameraPermission
import com.on99.elmcomposeui.component.navigation.Screen
import com.on99.elmcomposeui.component.nestedScrollLearning.NestComponent
import com.on99.elmcomposeui.ui.theme.ElmcomposeuiTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElmcomposeuiTheme {
                val shopsViewModel: ShopsViewModel =
                    viewModel(factory = ShopsViewModel.Factory)
                val detailUiState = shopsViewModel.detailUiState.collectAsState().value
                val navController = rememberNavController()
                val multiplePermissionsState = rememberMultiplePermissionsState(
                    listOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                    )
                )
                AnimatedVisibility(
                    visible = detailUiState.isCameraScreen,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(20f)
                ) {
                    CameraPermission(
                        multiplePermissionsState = multiplePermissionsState,
                        shopsViewModel = shopsViewModel
                    )
                }
                Scaffold(
                    bottomBar = {
                        BottomNavigationO(
                            navController = navController,
                            shopsViewModel = shopsViewModel
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Burger.route,
                        modifier = Modifier.padding(it)
                    ) {
                        composable(Screen.Burger.route) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize(),
                                color = MaterialTheme.colorScheme.background.copy(alpha = 1f)
                            ) {

                                HomeScreen(
                                    shopsViewModel = shopsViewModel,
                                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                                )

                                if (detailUiState.isDetailScreen) {
                                    DetailScreen(
                                        onBackPress = shopsViewModel::outShopCheck,
                                        componentDetailUiState = detailUiState
                                    )
                                }

                            }
                        }
                        composable(Screen.Flower.route) {
                            LoadingComponent()
                        }
                        composable(Screen.Hand.route) {
                            ErrorScreen(retryAction = { Log.e("Hand", "WWW") })
                        }
                        composable(Screen.Spaces.route) {
//                            Surface(modifier = Modifier.fillMaxSize(), color = Color.Red) {
//                                Button(
//                                    onClick = {},
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .background(
//                                            Color.DarkGray
//                                        )
//                                ) {
//                                    Text(
//                                        text = "Nice To Meet You Space Soilder",
//                                        fontSize = 35.sp,
//                                        modifier = Modifier.fillMaxWidth()
//                                    )
//                                }
//                            }
                            NestComponent()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    shopsViewModel: ShopsViewModel,
) {
    Column(
        modifier
            //.verticalScroll(rememberScrollState())
            .padding(vertical = 0.dp)
    ) {
        HomeTopBar(shopsViewModel = shopsViewModel)
//        Spacer(modifier = modifier.heightIn(min = 24.dp, max = 24.dp))
        SearchBar1(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 0.dp),
            stateViewModel = shopsViewModel
        )
        FirstBigLazyLine(Modifier.padding(start = 8.dp, end = 8.dp, top = 0.dp))
        SecondDoubleLazyGrid(
            Modifier
                .padding(start = 8.dp, end = 8.dp, top = 0.dp)
                .zIndex(1f)
                .background(color = Color.White)
        )
        ShopDetailsComponent(
//            shopUiState = shopsViewModel.shopUiState,
//            retryAction = shopsViewModel::getShopDetailVM,
            shopsViewModel = shopsViewModel
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ElmcomposeuiTheme {
        val navController = rememberNavController()
        val shopsViewModel: ShopsViewModel =
            viewModel(factory = ShopsViewModel.Factory)
        Scaffold(
            bottomBar = {
                BottomNavigationO(
                    navController = navController,
                    shopsViewModel = shopsViewModel
                )
            },
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Burger.route,
                modifier = Modifier.padding(it)
            ) {
                composable(Screen.Burger.route) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.2f)
                    ) {
                        //HomeScreen(shopsViewModel = shopsViewModel)
                    }
                }
                composable(Screen.Flower.route) {
                    LoadingComponent()
                }
                composable(Screen.Hand.route) {
                    ErrorScreen(retryAction = { Log.e("Hand", "WWW") })
                }
                composable(Screen.Spaces.route) {
                    Text(
                        text = "Nice To Meet You Space Soilder",
                        fontSize = 35.sp,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}