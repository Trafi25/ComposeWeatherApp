package com.plcoding.weatherapp.presentation

sealed interface WeatherAction {
    data object LoadWeather : WeatherAction
    data object Retry : WeatherAction
    data object ErrorDismissed : WeatherAction
    data object LocationPermissionGranted : WeatherAction
}
