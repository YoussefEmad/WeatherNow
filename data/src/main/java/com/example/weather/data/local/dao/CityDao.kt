package com.example.weather.data.local.dao

import androidx.room.*
import com.example.weather.data.local.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY lastSearched DESC LIMIT 1")
    fun getLastSearchedCity(): Flow<CityEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Query("DELETE FROM cities WHERE name = :cityName")
    suspend fun deleteCity(cityName: String)

    @Query("DELETE FROM cities")
    suspend fun deleteAllCities()
}

