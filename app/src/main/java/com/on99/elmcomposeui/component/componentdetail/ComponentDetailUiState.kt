package com.on99.elmcomposeui.component.componentdetail

import com.on99.elmcomposeui.R
import com.on99.elmcomposeui.component.navigation.Screen
import com.on99.elmcomposeui.model.ShopDetails

data class ComponentDetailUiState(
    /**
     * 用于指定被点击的单个条目的具体信息
     */
    val detail: ShopDetails = ShopDetails(
        id = "-1",
        imgsrc = "null",
        Title = "null",
        Descrition = "null"
    ),
    /**
     * 用于SearchBar的Camera展开
     */
    val isCameraScreen:Boolean = false,
    /**
     * 用于Home的LazyColumn条目详情
     */
    val isDetailScreen: Boolean = false,
    /**
     * 用于Home的Topbar的NavigationButton详情
     */
    val isHomeTopBarNavigationButtonScreen: Boolean = false,
    /**
     * 用于Home.Topbar.Message Button
     */
    val isHomeTopBarMessageButtonScreen: Boolean = false,
    /**
     * 以下是用于BottomBar资源
     */
    val items: List<Int> = listOf(
        R.string.bottom_navigation_01,
        R.string.bottom_navigation_02,
        R.string.bottom_navigation_03,
        R.string.bottom_navigation_04,
    ),
    val screenItems: List<Screen> = listOf(
        Screen.Burger,
        Screen.Flower,
        Screen.Hand,
        Screen.Spaces
    ),
    val paintResource: List<Int> = listOf(
        R.drawable.navibar_burger,
        R.drawable.navibar_flower,
        R.drawable.navibar_hand,
        R.drawable.navibar_ss
    ),
    val messageNumber: MutableList<String> = mutableListOf(
        (0..99).random().toString(),
        (0..99).random().toString(),
        (0..99).random().toString(),
        (0..99).random().toString()
    ),
    /**
     * LOCATION Detail
     */
    @Deprecated("wait compose update get LocationManager smoothly")
    val locationDetail: String = "---"
)