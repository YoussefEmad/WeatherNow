package com.example.weather.domain.model

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double
)

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class CurrentWeather(
    val name: String,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val sys: Sys,
    val visibility: Int
)

data class ForecastItem(
    val dt: Long,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind
)

data class City(
    val name: String
)

data class Forecast(
    val city: City,
    val list: List<ForecastItem>
)

data class CityEntity(
    val name: String,
    val lastSearched: Long = System.currentTimeMillis()
)




