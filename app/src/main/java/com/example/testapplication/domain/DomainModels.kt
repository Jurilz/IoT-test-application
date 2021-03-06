package com.example.testapplication.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Url(
    @PrimaryKey
    val value: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(primaryKeys = ["apiBase", "endpoint"])
data class SingleResponse(
    val apiBase: String,
    val endpoint: String,
    val _id: String?,
    val value: Float,
    val timestamp: String
)

@Entity(primaryKeys = ["apiBase", "endpoint"])
data class FlagResponse(
    val apiBase: String,
    val endpoint: String,
    val flag: String
)

@Entity(primaryKeys = ["apiBase", "timestamp"])
data class DomainMeasure(
    val apiBase: String,
    val _id: String?,
    val value: Float,
    val timestamp: Long
)

@Entity(primaryKeys = ["apiBase"])
data class ApiModel(
    val name: String,
    val description: String,
    val author: String?,
    val autor: String?,
    val apiBase: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(primaryKeys = ["name", "apiBase", "endpoint"])
data class Service(
    val name: String,
    val apiBase: String,
    val endpoint: String,
    val method: String,
    val description: String,
    val kind: String?,
    val actionLabel: String?,
    val timestamp: Long = System.currentTimeMillis()
)

enum class ServiceMethod {
    GET,
    POST,
    PUT,
    DELETE
}
