package com.example.weather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val name: String,
    val lastSearched: Long = System.currentTimeMillis()
)

