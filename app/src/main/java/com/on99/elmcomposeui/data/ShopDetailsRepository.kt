package com.on99.elmcomposeui.data

import com.on99.elmcomposeui.model.ShopDetails
import com.on99.elmcomposeui.model.TextBoard
import com.on99.elmcomposeui.network.ShopApiService

interface ShopDetailsRepository {
    suspend fun getShopDetails(): List<ShopDetails>
    suspend fun getTextBoards(): List<TextBoard>
}

class NetworkShopDetailsRepository(
    private val shopApiService: ShopApiService
) : ShopDetailsRepository {
    override suspend fun getShopDetails(): List<ShopDetails> = shopApiService.getShopDeatails()
    override suspend fun getTextBoards(): List<TextBoard> = shopApiService.getTextBoards()
}