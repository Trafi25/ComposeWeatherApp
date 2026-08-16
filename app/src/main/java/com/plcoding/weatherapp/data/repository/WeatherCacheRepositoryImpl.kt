package com.plcoding.weatherapp.data.repository

import com.plcoding.weatherapp.data.local.dao.CachedWeatherDao
import com.plcoding.weatherapp.domain.repository.WeatherCacheRepository
import javax.inject.Inject

internal class WeatherCacheRepositoryImpl
    @Inject
    constructor(
        private val cachedWeatherDao: CachedWeatherDao,
    ) : WeatherCacheRepository {
        override suspend fun clearWeatherCache() {
            cachedWeatherDao.clearAll()
        }
    }
