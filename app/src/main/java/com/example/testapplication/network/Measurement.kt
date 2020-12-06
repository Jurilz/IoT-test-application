package com.example.testapplication.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Measurement(
    val _id: String,
    val value: Int,
    val timestamp: Int
)
