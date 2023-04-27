package com.on99.elmcomposeui.model


@kotlinx.serialization.Serializable
data class TextBoard(
    val id: String,
    val text: String,
    val author: String,
    val date: String
)