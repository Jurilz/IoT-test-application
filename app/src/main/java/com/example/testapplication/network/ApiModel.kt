package com.example.testapplication.network

import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiModel(
    val name: String,
    val description: String,
    val author: String,
    val apiBase: String,
    @Json(name = "services")
    val services: List<Services>
)