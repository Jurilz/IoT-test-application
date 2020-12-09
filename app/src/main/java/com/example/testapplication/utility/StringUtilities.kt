package com.example.testapplication.utility

import org.ocpsoft.prettytime.PrettyTime
import java.util.*

fun formatTimestamp(timestamp: Long): String {
    val prettyTime = PrettyTime(Locale.getDefault())
    return prettyTime.format(Date(timestamp))
}