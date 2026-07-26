package com.plcoding.weatherapp.domain.repository

import com.plcoding.weatherapp.domain.location.City
import kotlinx.coroutines.flow.Flow

interface SavedCityRepository {
    fun observeSavedCities(): Flow<List<City>>

    suspend fun saveCity(city: City)

    suspend fun deleteCity(cityId: Int)

    suspend fun getCity(cityId: Int): City?
}
