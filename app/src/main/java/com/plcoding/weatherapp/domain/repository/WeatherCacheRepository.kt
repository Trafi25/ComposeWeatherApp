package com.plcoding.weatherapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface WeatherCacheRepository {
    suspend fun clearWeatherCache()
}
