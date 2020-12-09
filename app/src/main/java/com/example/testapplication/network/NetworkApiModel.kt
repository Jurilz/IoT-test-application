package com.example.testapplication.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

import com.example.testapplication.domain.ApiModel

@JsonClass(generateAdapter = true)
data class NetworkApiModel(
    val name: String,
    val description: String,
    val author: String,
    val apiBase: String,
    @Json(name = "services")
    val services: List<Services>
)

fun NetworkApiModel.asDomainApiModel() = ApiModel(
    name = name,
    description = description,
    author = author,
    apiBase = apiBase
)