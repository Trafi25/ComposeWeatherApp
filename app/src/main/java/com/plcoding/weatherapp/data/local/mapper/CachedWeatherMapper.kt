package com.plcoding.weatherapp.data.local.mapper

import com.plcoding.weatherapp.data.local.CachedWeatherEntity
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import com.squareup.moshi.Moshi
import javax.inject.Inject

class CachedWeatherMapper
    @Inject
    constructor(
        moshi: Moshi,
    ) {
        private val adapter =
            moshi.adapter(WeatherInfo::class.java)

        internal fun toEntity(
            weatherInfo: WeatherInfo,
            locationKey: String,
            latitude: Double,
            longitude: Double,
        ): CachedWeatherEntity =
            CachedWeatherEntity(
                locationKey = locationKey,
                latitude = latitude,
                longitude = longitude,
                weatherJson = adapter.toJson(weatherInfo),
                cachedAt = System.currentTimeMillis(),
            )

        internal fun toDomain(entity: CachedWeatherEntity): WeatherInfo? = adapter.fromJson(entity.weatherJson)
    }
