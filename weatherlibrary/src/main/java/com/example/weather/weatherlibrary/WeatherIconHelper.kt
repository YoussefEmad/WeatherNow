package com.example.weather.weatherlibrary

class WeatherIconHelper {

    companion object {
        private const val WEATHER_ICON_BASE_URL = "https://openweathermap.org/img/wn/"
    }

    fun getWeatherIconUrl(iconCode: String, size: IconSize = IconSize.LARGE): String {
        return "$WEATHER_ICON_BASE_URL${iconCode}${size.suffix}"
    }

    fun getWeatherIconUrl(iconCode: String, size: String): String {
        return "$WEATHER_ICON_BASE_URL${iconCode}@${size}x.png"
    }

    fun getWeatherConditionIcon(weatherId: Int): String {
        return when {
            weatherId in 200..299 -> "thunderstorm"
            weatherId in 300..399 -> "drizzle"
            weatherId in 500..599 -> "rain"
            weatherId in 600..699 -> "snow"
            weatherId in 700..799 -> "atmosphere"
            weatherId == 800 -> "clear"
            weatherId in 801..899 -> "clouds"
            else -> "unknown"
        }
    }

    enum class IconSize(val suffix: String) {
        SMALL("@1x.png"),
        MEDIUM("@2x.png"),
        LARGE("@4x.png")
    }
}
