package com.example.weather.di

import android.content.Context
import com.example.weather.data.local.WeatherDatabase
import com.example.weather.data.local.dao.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return WeatherDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideCityDao(database: WeatherDatabase): CityDao {
        return database.cityDao()
    }
}





