package com.on99.elmcomposeui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.on99.elmcomposeui.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationO(
    modifier: Modifier = Modifier,
    navController: NavController,
    shopsViewModel: ShopsViewModel
) {
    var selectedItem by remember {
        mutableStateOf(0)
    }
//    val items = listOf(
//        stringResource(id = R.string.bottom_navigation_01),
//        stringResource(id = R.string.bottom_navigation_02),
//        stringResource(id = R.string.bottom_navigation_03),
//        stringResource(id = R.string.bottom_navigation_04)
//    )
//    val screenItems = listOf(
//        Screen.Burger,
//        Screen.Flower,
//        Screen.Hand,
//        Screen.Spaces
//    )
//    val paintngResource = listOf(
//        R.drawable.navibar_burger,
//        R.drawable.navibar_flower,
//        R.drawable.navibar_hand,
//        R.drawable.navibar_ss
//    )
//    var messageNumber = mutableListOf(
//        (0..99).random().toString(),
//        (0..99).random().toString(),
//        (0..99).random().toString(),
//        (0..99).random().toString()
//    )
    val detailUiState = shopsViewModel.detailUiState.collectAsState().value
    val items = detailUiState.items
    val screenItems = detailUiState.screenItems
    val paintngResource = detailUiState.paintResource
    val messageNumber = detailUiState.messageNumber

    NavigationBar(
        modifier = modifier
            .height(70.dp)
            .padding(bottom = 0.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        //val density = LocalDensity.current
        Log.e("windowInsets", "-> ${NavigationBarDefaults.windowInsets}")
        items.forEachIndexed { index, s ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    shopsViewModel.clearTheBadgeNumber(index = index)
                    shopsViewModel.outShopCheck()
                    shopsViewModel.switchMessageButtonScreenStateFalse()
                    shopsViewModel.switchNavigationButtonScreenStateFalse()
                    navController.navigate(screenItems[index].route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    BadgedBox(badge = {
                        val badgeNumber = if (selectedItem != index) {
                            messageNumber[index]
                        } else {
                            ""
                        }
                        Text(
                            text = if (badgeNumber == "0") " " else badgeNumber, modifier = Modifier
                                .semantics {
                                    contentDescription = "$badgeNumber new notification"
                                }
                                .background(color = Color(0xFFE59E9A), shape = CircleShape)
                                .clip(CircleShape)
                                .border(
                                    width = 1.dp,
                                    color = Color.Transparent,
                                    shape = CircleShape
                                ),
                            fontSize = 13.sp
                        )
                    }) {
                        if (selectedItem != index) {
                            Icon(
                                painter = painterResource(id = paintngResource[index]),
                                contentDescription = stringResource(id = s),
                                //modifier = Modifier.size(50.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = paintngResource[index]),
                                contentDescription = stringResource(id = s),
                                //modifier = Modifier.size(50.dp),
                                tint = Color.Blue
                            )
                        }
                    }
                },
//                icon = {
//                    if (selectedItem != index){
//                        Icon(
//                            painter = painterResource(id = paintngResource[index]),
//                            contentDescription = s
//                        )
//                    }
//                    else{
//                        Icon(
//                            painter = painterResource(id = paintngResource[index]),
//                            contentDescription = s,
//                            tint = Color.Blue
//                        )
//                    }
//                },
                label = {
                    if (selectedItem != index)
                        Text(text = stringResource(id = s))
                    else
                        Text(text = "")
                },
                //alwaysShowLabel = selectedItem != index
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BottomNavigation0Preview() {
    var selectedItem by remember {
        mutableStateOf(0)
    }
    val items = listOf(
        stringResource(id = R.string.bottom_navigation_01),
        stringResource(id = R.string.bottom_navigation_02),
        stringResource(id = R.string.bottom_navigation_03),
        stringResource(id = R.string.bottom_navigation_04)
    )
    val paintngResource = listOf(
        R.drawable.navibar_burger,
        R.drawable.navibar_flower,
        R.drawable.navibar_hand,
        R.drawable.navibar_ss
    )
    val messageNumber = mutableListOf(
        (0..99).random().toString(),
        (0..99).random().toString(),
        (0..99).random().toString(),
        (0..99).random().toString()
    )

    NavigationBar(
        modifier = Modifier
            //.height(70.dp)
            .padding(bottom = 0.dp),
        containerColor = Color.LightGray
    ) {
        Log.e("windowInsets", "-> ${NavigationBarDefaults.windowInsets}")
        items.forEachIndexed { index, s ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    messageNumber[selectedItem] = ""
                },
                icon = {
                    BadgedBox(badge = {
                        val badgeNumber = if (selectedItem != index) {
                            messageNumber[index]
                        } else {
                            ""
                        }
                        Text(
                            text = if (badgeNumber == "0") " " else badgeNumber, modifier = Modifier
                                .semantics {
                                    contentDescription = "$badgeNumber new notification"
                                }
                                .background(color = Color.Red, shape = CircleShape)
                                .clip(CircleShape)
                                .border(
                                    width = 1.dp,
                                    color = Color.Transparent,
                                    shape = CircleShape
                                ),
                            fontSize = 13.sp
                        )
                    }) {
                        if (selectedItem != index) {
                            Icon(
                                painter = painterResource(id = paintngResource[index]),
                                modifier = Modifier.size(30.dp),
                                contentDescription = s
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = paintngResource[index]),
                                modifier = Modifier.size(30.dp),
                                contentDescription = s,
                                tint = Color.Blue
                            )
                        }
                    }
                },
//                icon = {
//                    if (selectedItem != index){
//                        Icon(
//                            painter = painterResource(id = paintngResource[index]),
//                            contentDescription = s
//                        )
//                    }
//                    else{
//                        Icon(
//                            painter = painterResource(id = paintngResource[index]),
//                            contentDescription = s,
//                            tint = Color.Blue
//                        )
//                    }
//                },
                label = {
                    val tempText:String = if(selectedItem != index){
                        s
                    }else{
                        ""
                    }
                    Text(text = tempText, fontSize = 25.sp)
                },
                modifier = Modifier.padding(bottom = 0.dp)
                //alwaysShowLabel = selectedItem != index
            )
        }
    }
}