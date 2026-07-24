package com.plcoding.weatherapp.presentation.weather.states

import androidx.compose.runtime.Immutable
import com.plcoding.weatherapp.domain.weather.WeatherInfo

@Immutable
data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val locationName: String? = null,
    val screenMode: WeatherScreenMode = WeatherScreenMode.Weather,
    val citySearchState: CitySearchState = CitySearchState(),
)
