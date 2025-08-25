package com.example.weather.features.forecast

sealed class ForecastEffect {
    object NavigateBack : ForecastEffect()
    data class ShowError(val message: String) : ForecastEffect()
}

