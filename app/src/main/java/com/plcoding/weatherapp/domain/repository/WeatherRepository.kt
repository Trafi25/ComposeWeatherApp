package com.plcoding.weatherapp.domain.repository

import com.plcoding.weatherapp.domain.weather.WeatherInfo
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.util.WeatherError

interface WeatherRepository {
    suspend fun getWeatherData(
        lat: Double,
        long: Double,
    ): Result<WeatherInfo, WeatherError>
}
