package com.plcoding.weatherapp.data.repository

import com.plcoding.weatherapp.data.mappers.toCityDomain
import com.plcoding.weatherapp.data.remote.GeoCodingApi
import com.plcoding.weatherapp.data.remote.dtos.CityDto
import com.plcoding.weatherapp.data.util.toWeatherError
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.repository.CityRepository
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.util.WeatherError
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val api : GeoCodingApi
): CityRepository {
    override suspend fun searchCities(query: String): Result<List<City>, WeatherError> {
        return try {
            val citys = api.searchCities(query).results.orEmpty().map(CityDto::toCityDomain)

            Result.Success(citys)
        } catch (exception : Exception){
            Result.Error(error = exception.toWeatherError())
        }

    }
}
