package com.plcoding.weatherapp.presentation.weather.state

/**
 * represents one-time events that happen on the Weather screen,
 * such as navigation or showing temporary UI like SnackBar or Permissions.
 */
sealed interface WeatherEffect {
    data class ShowSnackbar(
        val message: String,
    ) : WeatherEffect

    data object RequestLocationPermission : WeatherEffect

    data object RequestNotificationPermission : WeatherEffect
}
