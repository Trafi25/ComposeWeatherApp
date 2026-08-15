package com.plcoding.weatherapp.presentation.weather.city

import com.plcoding.weatherapp.domain.location.City

sealed interface CityManagerAction {
    data class CitySelected(val city: City) : CityManagerAction
    data class CityDeleted(val cityId: Int) : CityManagerAction
    data object CurrentLocationSelected : CityManagerAction
    data object AddCityClicked : CityManagerAction
    data object BackClicked : CityManagerAction
}
