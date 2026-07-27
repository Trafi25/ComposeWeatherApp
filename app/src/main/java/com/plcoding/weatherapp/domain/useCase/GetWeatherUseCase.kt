package com.plcoding.weatherapp.domain.useCase

import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.DataError
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

/**
 * Use Case to fetch weather data.
 *
 * Why: Encapsulates the core action of the app.
 * Any special sorting or filtering of weather data (like the 24-hour limit)
 * should live here, not in the ViewModel.
 */
class GetWeatherUseCase
    @Inject
    constructor(
        private val repository: WeatherRepository,
    ) {
        suspend operator fun invoke(
            lat: Double,
            long: Double,
        ): Result<WeatherInfo, DataError> = repository.getWeatherData(lat, long)
    }
