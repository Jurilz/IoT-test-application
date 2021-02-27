package com.example.testapplication.utility

import java.util.*
import org.ocpsoft.prettytime.PrettyTime

fun formatTimestamp(timestamp: Long): String {
    val prettyTime = PrettyTime(Locale.getDefault())
    return prettyTime.format(Date(timestamp * 1000L))
}
