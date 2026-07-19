package com.plcoding.weatherapp.domain.weather

import java.time.LocalDateTime

data class CurrentWeatherData(
    val time: LocalDateTime,
    val temperatureCelsius: Double,
    val apparentTemperatureCelsius: Double,
    val humidity: Double,
    val pressure: Double,
    val precipitationMm: Double,
    val cloudCoverPercent: Double,
    val windSpeed: Double,
    val windDirectionDegrees: Int,
    val windDirectionLabel: String,
    val windGustsKmh: Double,
    val isDay: Boolean,
    val weatherType: WeatherType,
)
