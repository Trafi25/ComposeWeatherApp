package com.plcoding.weatherapp.domain.repository

import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.util.DataError
import com.plcoding.weatherapp.domain.util.Result

interface CityRepository {
    suspend fun searchCities(query: String): Result<List<City>, DataError>
}
