package com.plcoding.weatherapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface SelectedLocationRepository {
    fun observeSelectedCityId(): Flow<Int?>

    suspend fun saveSelectedCityId(cityId: Int)

    suspend fun selectCurrentLocation()
}
