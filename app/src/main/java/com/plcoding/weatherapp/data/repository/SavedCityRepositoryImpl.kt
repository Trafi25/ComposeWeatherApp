package com.plcoding.weatherapp.data.repository

import com.plcoding.weatherapp.data.local.dao.SavedCityDao
import com.plcoding.weatherapp.data.local.mapper.toDomain
import com.plcoding.weatherapp.data.local.mapper.toEntity
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.repository.SavedCityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SavedCityRepositoryImpl
    @Inject
    constructor(
        private val savedCityDao: SavedCityDao,
    ) : SavedCityRepository {
        override fun observeSavedCities(): Flow<List<City>> =
            savedCityDao.observeSavedCities().map { entities ->
                entities.map { entity -> entity.toDomain() }
            }

        override suspend fun saveCity(city: City) {
            savedCityDao.insertCity(city.toEntity())
        }

        override suspend fun deleteCity(cityId: Int) {
            savedCityDao.deleteCityById(cityId)
        }

        override suspend fun getCity(cityId: Int): City? = savedCityDao.getCityById(cityId)?.toDomain()
    }
