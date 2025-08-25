package com.example.weather.domain.usecase

import com.example.weather.core.Result
import com.example.weather.domain.model.Forecast
import com.example.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): Result<Forecast> {
        if (cityName.isBlank()) {
            return Result.Error(IllegalArgumentException("City name cannot be empty"))
        }
        return repository.getForecast(cityName)
    }
}
