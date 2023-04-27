package com.on99.elmcomposeui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShopDetails(
    val id: String,
    @SerialName(value = "imgsrc")
    val imgsrc: String,
    @SerialName(value = "title")
    val Title: String,
    @SerialName(value = "descrition")
    val Descrition: String
)

