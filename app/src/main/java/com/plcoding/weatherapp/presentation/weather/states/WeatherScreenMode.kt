package com.plcoding.weatherapp.presentation.weather.states

sealed interface WeatherScreenMode {
    data object Weather : WeatherScreenMode
    data object SearchCity : WeatherScreenMode
    data object ManageCities : WeatherScreenMode
}
