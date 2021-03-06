package com.example.testapplication.network

import com.example.testapplication.domain.DomainMeasure
import com.example.testapplication.domain.SingleResponse
import com.example.testapplication.utility.formatTimestamp
import com.squareup.moshi.JsonClass

private const val MS_FACTOR: Long = 1000L

@JsonClass(generateAdapter = true)
data class Measurement(
    val _id: String?,
    val value: Float,
    val timestamp: Long
)

fun Measurement.asDomainSingle(apiBase: String, endpoint: String) = SingleResponse(
    apiBase = apiBase,
    endpoint = endpoint,
    _id = _id,
    value = value,
    timestamp = formatTimestamp(timestamp)
)

fun Measurement.asDomainMeasurement(apiBase: String) = DomainMeasure(
    apiBase = apiBase,
    _id = _id,
    value = value,
    timestamp = timestamp * MS_FACTOR
)
