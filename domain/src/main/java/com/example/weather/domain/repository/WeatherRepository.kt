package com.example.weather.domain.repository

import com.example.weather.core.Result
import com.example.weather.domain.model.CityEntity
import com.example.weather.domain.model.CurrentWeather
import com.example.weather.domain.model.Forecast
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(cityName: String): Result<CurrentWeather>
    suspend fun getForecast(cityName: String): Result<Forecast>
    fun getLastSearchedCity(): Flow<CityEntity?>
    suspend fun saveCity(cityName: String)
}
