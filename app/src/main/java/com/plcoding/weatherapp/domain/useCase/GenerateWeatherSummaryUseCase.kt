package com.plcoding.weatherapp.domain.usecase

import com.plcoding.weatherapp.domain.repository.WeatherAiRepository
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

class GenerateWeatherSummaryUseCase
    @Inject
    constructor(
        private val repository: WeatherAiRepository,
    ) {
        suspend operator fun invoke(
            weatherInfo: WeatherInfo,
            locationName: String,
            latitude: Double,
            longitude: Double,
        ): String =
            repository.generateWeatherSummary(
                weatherInfo = weatherInfo,
                locationName = locationName,
                latitude = latitude,
                longitude = longitude,
            )
    }
