package com.plcoding.weatherapp.presentation

import androidx.compose.runtime.Immutable
import com.plcoding.weatherapp.domain.weather.WeatherInfo

@Immutable
data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val locationName: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasLocationPermission: Boolean = false,
)
