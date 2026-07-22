package com.plcoding.weatherapp.data.remote.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyWeatherDto(
    val time: List<String>,
    @Json(name = "weather_code")
    val weatherCodes: List<Int>,
    @Json(name = "temperature_2m_max")
    val maximumTemperatures: List<Double>,
    @Json(name = "temperature_2m_min")
    val minimumTemperatures: List<Double>,
    @Json(name = "precipitation_probability_max")
    val precipitationProbabilities: List<Int>,
)
