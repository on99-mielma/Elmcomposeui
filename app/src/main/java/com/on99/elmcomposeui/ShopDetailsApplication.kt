package com.on99.elmcomposeui

import android.app.Application
import com.on99.elmcomposeui.data.DefaultShopContainer
import com.on99.elmcomposeui.data.ShopContainer

class ShopDetailsApplication:Application() {
    lateinit var container: ShopContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultShopContainer()
    }
}