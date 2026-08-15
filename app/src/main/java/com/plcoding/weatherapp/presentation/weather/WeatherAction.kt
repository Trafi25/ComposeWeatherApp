package com.plcoding.weatherapp.presentation.weather

sealed interface WeatherAction {
    data object Retry : WeatherAction

    data object Refresh : WeatherAction

    data object ErrorDismissed : WeatherAction

    data object RequestLocationPermission : WeatherAction

    data object LocationPermissionGranted : WeatherAction

    data object LocationPermissionDenied : WeatherAction

    // Main Navigation
    data object SearchCityClicked : WeatherAction

    data object ManageCitiesClicked : WeatherAction

    data object SettingsClicked : WeatherAction

    data object BackClicked : WeatherAction
}
