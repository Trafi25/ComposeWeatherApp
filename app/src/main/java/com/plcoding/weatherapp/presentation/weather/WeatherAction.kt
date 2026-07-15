package com.plcoding.weatherapp.presentation.weather

sealed interface WeatherAction {
    data object LoadWeather : WeatherAction

    data object Retry : WeatherAction

    data object RequestLocationPermission : WeatherAction

    data object LocationPermissionDenied : WeatherAction

    data object LocationPermissionGranted : WeatherAction

    data object ErrorDismissed : WeatherAction
}
