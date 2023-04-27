package com.on99.elmcomposeui.component.navigation

import androidx.annotation.StringRes
import com.on99.elmcomposeui.R

sealed class Screen (val route:String,@StringRes val resourceId:Int){
    object Burger : Screen("burger", R.string.bottom_navigation_01)
    object Flower : Screen("flower", R.string.bottom_navigation_02)
    object Hand : Screen("hand", R.string.bottom_navigation_03)
    object Spaces : Screen("spaces", R.string.bottom_navigation_04)
}