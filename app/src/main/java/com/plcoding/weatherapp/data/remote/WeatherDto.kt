package com.plcoding.weatherapp.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherDto(
    @field:Json(name = "hourly")
    val hourlyWeatherData: WeatherDataDto,
    @field:Json(name = "current")
    val currentWeatherData: CurrentWeatherDto,
)
