package com.example.weather.weatherlibrary

class WeatherFormatter {

    fun formatTemperature(temp: Double): String {
        val celsius = temp.kelvinToCelsius()
        return "%.1f°C".format(celsius)
    }

    fun formatWeatherDescription(description: String): String {
        return description.capitalizeFirst()
    }

    fun formatHumidity(humidity: Int): String {
        return "$humidity%"
    }

    fun formatPressure(pressure: Int): String {
        return "${pressure}hPa"
    }

    fun formatWindSpeed(speed: Double): String {
        val kmh = speed * 3.6
        return "%.1f km/h".format(kmh)
    }

    private fun Double.kelvinToCelsius(): Double = this

    private fun String.capitalizeFirst(): String {
        return if (this.isNotEmpty()) {
            this[0].uppercase() + this.substring(1)
        } else {
            this
        }
    }
}
