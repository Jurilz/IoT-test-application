package com.example.testapplication.utility

import com.example.testapplication.domain.FlagResponse

fun Boolean.asDomainFlag(apiBase: String, endpoint: String) = FlagResponse(
    apiBase = apiBase,
    endpoint = endpoint,
    flag = this.toString()
)
