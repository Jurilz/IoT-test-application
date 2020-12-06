package com.example.testapplication.network

data class ServiceResponse(
    val service: Services,
    val status: Boolean?,
    val single: Measurement?,
    val timeseries: List<Measurement>?
)
