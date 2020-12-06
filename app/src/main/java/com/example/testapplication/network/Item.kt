package com.example.testapplication.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Item(
    val label: String,
    val type: String
)
