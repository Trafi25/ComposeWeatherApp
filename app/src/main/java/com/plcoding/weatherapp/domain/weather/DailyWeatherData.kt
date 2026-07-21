package com.plcoding.weatherapp.domain.weather

import java.time.LocalDate

data class DailyWeatherData(
    val date: LocalDate,
    val weatherType: WeatherType,
    val minimumTemperature: Double,
    val maximumTemperature: Double,
    val precipitationProbabilityPercent: Int,
)
