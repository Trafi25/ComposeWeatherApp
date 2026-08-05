package com.plcoding.weatherapp.presentation.weather.state

sealed interface WeatherScreenMode {
    data object Weather : WeatherScreenMode

    data object SearchCity : WeatherScreenMode

    data object ManageCities : WeatherScreenMode

    data object Settings : WeatherScreenMode
}
