package com.plcoding.weatherapp.domain.repository

interface WeatherCacheRepository {
    suspend fun clearWeatherCache()
}
