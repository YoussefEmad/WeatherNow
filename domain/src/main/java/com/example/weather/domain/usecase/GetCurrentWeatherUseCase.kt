package com.example.weather.domain.usecase

import com.example.weather.core.Result
import com.example.weather.domain.model.CurrentWeather
import com.example.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): Result<CurrentWeather> {
        if (cityName.isBlank()) {
            return Result.Error(IllegalArgumentException("City name cannot be empty"))
        }
        return repository.getCurrentWeather(cityName)
    }
}
