package com.example.weather.data.repository

import com.example.weather.core.Constants
import com.example.weather.core.Result
import com.example.weather.data.api.WeatherApi
import com.example.weather.data.local.dao.CityDao
import com.example.weather.data.local.entity.CityEntity as DataCityEntity
import com.example.weather.data.model.CurrentWeatherResponse
import com.example.weather.data.model.ForecastResponse
import com.example.weather.domain.model.CityEntity
import com.example.weather.domain.model.CurrentWeather
import com.example.weather.domain.model.Forecast
import com.example.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val cityDao: CityDao
) : WeatherRepository {

    override suspend fun getCurrentWeather(cityName: String): Result<CurrentWeather> {
        return try {
            val response = weatherApi.getCurrentWeather(
                cityName = cityName,
                apiKey = "4997de5a629065cfeb601c5e14b8fe08",
                units = "metric"
            )
            cityDao.insertCity(DataCityEntity(cityName))
            Result.Success(response.toDomainModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getForecast(cityName: String): Result<Forecast> {
        return try {
            val response = weatherApi.getForecast(
                cityName = cityName,
                apiKey = Constants.OPENWEATHER_API_KEY,
                units = "metric"
            )
            Result.Success(response.toDomainModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getLastSearchedCity(): Flow<CityEntity?> {
        return cityDao.getLastSearchedCity().map { dataEntity ->
            dataEntity?.toDomainModel()
        }
    }

    override suspend fun saveCity(cityName: String) {
        cityDao.insertCity(DataCityEntity(cityName))
    }


    private fun CurrentWeatherResponse.toDomainModel(): CurrentWeather {
        return CurrentWeather(
            name = name,
            weather = weather.map { it.toDomainModel() },
            main = main.toDomainModel(),
            wind = wind.toDomainModel(),
            sys = sys.toDomainModel(),
            visibility = visibility
        )
    }

    private fun ForecastResponse.toDomainModel(): Forecast {
        return Forecast(
            city = city.toDomainModel(),
            list = list.map { it.toDomainModel() }
        )
    }

    private fun com.example.weather.data.model.Weather.toDomainModel(): com.example.weather.domain.model.Weather {
        return com.example.weather.domain.model.Weather(
            id = id,
            main = main,
            description = description,
            icon = icon
        )
    }

    private fun com.example.weather.data.model.Main.toDomainModel(): com.example.weather.domain.model.Main {
        return com.example.weather.domain.model.Main(
            temp = temp,
            feelsLike = feelsLike,
            tempMin = tempMin,
            tempMax = tempMax,
            pressure = pressure,
            humidity = humidity
        )
    }

    private fun com.example.weather.data.model.Wind.toDomainModel(): com.example.weather.domain.model.Wind {
        return com.example.weather.domain.model.Wind(speed = speed)
    }

    private fun com.example.weather.data.model.Sys.toDomainModel(): com.example.weather.domain.model.Sys {
        return com.example.weather.domain.model.Sys(
            country = country,
            sunrise = sunrise,
            sunset = sunset
        )
    }

    private fun com.example.weather.data.model.ForecastItem.toDomainModel(): com.example.weather.domain.model.ForecastItem {
        return com.example.weather.domain.model.ForecastItem(
            dt = dt,
            weather = weather.map { it.toDomainModel() },
            main = main.toDomainModel(),
            wind = wind.toDomainModel()
        )
    }

    private fun com.example.weather.data.model.City.toDomainModel(): com.example.weather.domain.model.City {
        return com.example.weather.domain.model.City(name = name)
    }

    private fun DataCityEntity.toDomainModel(): CityEntity {
        return CityEntity(name = name, lastSearched = lastSearched)
    }
}
