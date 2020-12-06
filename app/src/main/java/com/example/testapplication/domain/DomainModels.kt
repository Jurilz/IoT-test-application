package com.example.testapplication.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Url(
    @PrimaryKey
    val value: String,
    val timestamp: Long = System.currentTimeMillis()
)