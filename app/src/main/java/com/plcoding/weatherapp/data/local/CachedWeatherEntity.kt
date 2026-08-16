package com.plcoding.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Locale

@Entity(tableName = "cached_weather")
internal data class CachedWeatherEntity(
    @PrimaryKey
    val locationKey: String,
    val latitude: Double,
    val longitude: Double,
    val weatherJson: String,
    val cachedAt: Long,
)

fun weatherLocationKey(
    latitude: Double,
    longitude: Double,
): String =
    String.format(
        Locale.US,
        "%.4f,%.4f",
        latitude,
        longitude,
    )
