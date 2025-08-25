package com.example.weather.features.forecast

import com.example.weather.domain.model.Forecast

data class ForecastState(
    val isLoading: Boolean = false,
    val forecastData: Forecast? = null,
    val error: String? = null,
    val cityName: String = ""
)





