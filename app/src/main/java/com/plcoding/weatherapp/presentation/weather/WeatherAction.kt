package com.plcoding.weatherapp.presentation.weather

import com.plcoding.weatherapp.domain.location.City

sealed interface WeatherAction {
    data object LoadWeather : WeatherAction

    data object Retry : WeatherAction

    data object RequestLocationPermission : WeatherAction

    data object LocationPermissionDenied : WeatherAction

    data object LocationPermissionGranted : WeatherAction

    data object CurrentLocationSelected : WeatherAction

    data object ErrorDismissed : WeatherAction

    // City navigation
    data object SearchCityClicked : WeatherAction

    data object ManageCitiesClicked : WeatherAction

    data object CityScreenBackClicked : WeatherAction

    data class SearchQueryChanged(
        val query: String,
    ) : WeatherAction

    data class CitySelected(
        val city: City,
    ) : WeatherAction

    data class SavedCityDeleted(
        val cityId: Int,
    ) : WeatherAction
}
