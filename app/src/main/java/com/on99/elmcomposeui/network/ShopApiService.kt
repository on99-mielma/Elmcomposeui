package com.on99.elmcomposeui.network

import com.on99.elmcomposeui.model.ShopDetails
import com.on99.elmcomposeui.model.TextBoard
import retrofit2.http.GET

interface ShopApiService {
    @GET("elm")
    suspend fun getShopDeatails():List<ShopDetails>

    @GET("tb/token")
    suspend fun getTextBoards():List<TextBoard>
}