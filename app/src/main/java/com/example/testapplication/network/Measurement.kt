package com.example.testapplication.network

import com.squareup.moshi.JsonClass
import com.example.testapplication.domain.SingleResponse
import com.example.testapplication.utility.formatTimestamp

@JsonClass(generateAdapter = true)
data class Measurement(
    val _id: String,
    val value: Int,
    val timestamp: Long
)

fun Measurement.asDomainSingle(apiBase: String, endpoint: String) = SingleResponse(
    apiBase = apiBase,
    endpoint = endpoint,
    _id = _id,
    value = value,
    timestamp = formatTimestamp(timestamp)
)
