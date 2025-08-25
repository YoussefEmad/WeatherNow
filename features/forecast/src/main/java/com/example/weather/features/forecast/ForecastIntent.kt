package com.example.weather.features.forecast

sealed class ForecastIntent {
    object LoadForecast : ForecastIntent()
    object RefreshForecast : ForecastIntent()
    data class RetryLoad(val cityName: String) : ForecastIntent()
}




