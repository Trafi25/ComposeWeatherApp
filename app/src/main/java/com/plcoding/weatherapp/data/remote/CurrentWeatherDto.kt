package com.plcoding.weatherapp.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherDto(
    val time: String,
    @field:Json(name = "temperature_2m")
    val temperature: Double,
    @field:Json(name = "weather_code")
    val weatherCode: Int,
    @field:Json(name = "pressure_msl")
    val pressure: Double,
    @field:Json(name = "wind_speed_10m")
    val windSpeed: Double,
    @field:Json(name = "relative_humidity_2m")
    val humidity: Double,
    @field:Json(name = "is_day")
    val isDay: Int,
)
