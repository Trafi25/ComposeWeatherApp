package com.plcoding.weatherapp.data.repository

import com.plcoding.weatherapp.data.mappers.toWeatherInfo
import com.plcoding.weatherapp.data.remote.WeatherApi
import com.plcoding.weatherapp.data.util.toWeatherError
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.util.WeatherError
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl
    @Inject
    constructor(
        private val api: WeatherApi,
    ) : WeatherRepository {
        override suspend fun getWeatherData(
            lat: Double,
            long: Double,
        ): Result<WeatherInfo, WeatherError> =
            try {
                val weatherInfo =
                    api
                        .getWeatherData(
                            lat = lat,
                            long = long,
                        ).toWeatherInfo()

                Result.Success(weatherInfo)
            } catch (exception: Exception) {
                Result.Error(
                    error = exception.toWeatherError(),
                )
            }
    }
