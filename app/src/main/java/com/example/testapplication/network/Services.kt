package com.example.testapplication.network

import com.example.testapplication.domain.Service
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Services(
    val name: String,
    val endpoint: String,
    val method: String,
    val description: String,
    val kind: String?,
    val actionLabel: String?,
    @Json(name = "body")
    val body: List<Body>?,
    @Json(name = "query")
    val query: List<Query>?,
    @Json(name = "param")
    val param: List<Param>?,
    val example: String
)

fun Services.asDomainService(apiBase: String) = Service(
    name = name,
    apiBase = apiBase,
    endpoint = endpoint,
    method = method,
    description = description,
    actionLabel = actionLabel,
    kind = kind
)
