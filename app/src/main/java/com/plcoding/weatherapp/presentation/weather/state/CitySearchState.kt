package com.plcoding.weatherapp.presentation.weather.state

import androidx.compose.runtime.Immutable
import com.plcoding.weatherapp.domain.location.City

@Immutable
data class CitySearchState(
    val query: String = "",
    val results: List<City> = emptyList(),
    val isLoading: Boolean = false,
    val isVisible: Boolean = false,
    val errorMessage: String? = null,
)
