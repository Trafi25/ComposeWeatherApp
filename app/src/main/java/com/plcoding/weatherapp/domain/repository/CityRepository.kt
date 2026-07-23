package com.plcoding.weatherapp.domain.repository

import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.util.WeatherError

interface CityRepository {

    suspend fun searchCities(query: String) : Result<List<City>, WeatherError>
}
