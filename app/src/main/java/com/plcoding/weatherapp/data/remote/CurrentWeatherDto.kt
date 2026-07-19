package com.plcoding.weatherapp.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherDto(
    val time: String,
    @field:Json(name = "temperature_2m")
    val temperature: Double,
    @field:Json(name = "apparent_temperature")
    val apparentTemperature: Double,
    @field:Json(name = "precipitation")
    val precipitation: Double,
    @field:Json(name = "cloud_cover")
    val cloudCover: Double,
    @field:Json(name = "weather_code")
    val weatherCode: Int,
    @field:Json(name = "pressure_msl")
    val pressure: Double,
    @field:Json(name = "wind_speed_10m")
    val windSpeed: Double,
    @field:Json(name = "wind_direction_10m")
    val windDirection: Int,
    @field:Json(name = "wind_gusts_10m")
    val windGusts: Double,
    @field:Json(name = "relative_humidity_2m")
    val humidity: Double,
    @field:Json(name = "is_day")
    val isDay: Int,
)
