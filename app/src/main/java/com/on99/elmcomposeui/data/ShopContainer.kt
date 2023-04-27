package com.on99.elmcomposeui.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.on99.elmcomposeui.network.ShopApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface ShopContainer{
    val shopDetailsRepository:ShopDetailsRepository
}

class DefaultShopContainer:ShopContainer{
    private val BASE_URL = "http://192.168.31.233:8999/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()
    private val retrofitService:ShopApiService by lazy {
        retrofit.create(ShopApiService::class.java)
    }
    override val shopDetailsRepository: ShopDetailsRepository by lazy {
        NetworkShopDetailsRepository(retrofitService)
    }

}