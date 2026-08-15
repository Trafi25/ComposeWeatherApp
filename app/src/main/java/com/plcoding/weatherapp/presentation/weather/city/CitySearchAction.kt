package com.plcoding.weatherapp.presentation.weather.city

import com.plcoding.weatherapp.domain.location.City

sealed interface CitySearchAction {
    data class QueryChanged(val query: String) : CitySearchAction
    data class CitySelected(val city: City) : CitySearchAction
    data object ClearQuery : CitySearchAction
    data object BackClicked : CitySearchAction
}
