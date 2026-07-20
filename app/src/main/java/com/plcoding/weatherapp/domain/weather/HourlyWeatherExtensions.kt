package com.plcoding.weatherapp.domain.weather

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun List<WeatherData>.upcomingHours(
    now: LocalDateTime = LocalDateTime.now(),
    count: Int = 24,
): List<WeatherData> {
    val currentHour = now.truncatedTo(ChronoUnit.HOURS)
    return asSequence()
        .filter { weatherData ->
            !weatherData.time.isBefore(currentHour)
        }.take(count)
        .toList()
}
