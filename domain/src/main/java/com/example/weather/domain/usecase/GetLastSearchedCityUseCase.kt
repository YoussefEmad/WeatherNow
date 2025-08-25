package com.example.weather.domain.usecase

import com.example.weather.domain.model.CityEntity
import com.example.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastSearchedCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<CityEntity?> {
        return repository.getLastSearchedCity()
    }
}
