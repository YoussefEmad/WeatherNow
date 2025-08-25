package com.example.weather.core.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateString(pattern: String = "EEE, MMM dd"): String {
    val date = Date(this * 1000)
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date)
}

fun Double.kelvinToCelsius(): Double = this  -273.15

fun Double.kelvinToFahrenheit(): Double = (this - 273.15) * 9/5 + 32

fun String.capitalizeFirst(): String {
    return if (this.isNotEmpty()) {
        this[0].uppercase() + this.substring(1)
    } else {
        this
    }
}

