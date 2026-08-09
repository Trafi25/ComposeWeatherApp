package com.plcoding.weatherapp.domain.repository

import com.plcoding.weatherapp.domain.weather.WeatherInfo

interface WeatherAiRepository {

    suspend fun generateWeatherSummary(
        weatherInfo: WeatherInfo,
        locationName : String
    ): String
}
