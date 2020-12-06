package com.example.testapplication.network

import androidx.room.Entity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Query(
    val label: String,
    val type: String
)