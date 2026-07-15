package com.plcoding.weatherapp.domain.location

interface LocationNameResolver {
    suspend fun getLocationName(
        latitude: Double,
        longitude: Double,
    ): String?
}
