package com.plcoding.weatherapp.domain.util

sealed interface WeatherError {
    data object NoInternet : WeatherError

    data object Unauthorized : WeatherError

    data object LocationUnavailable : WeatherError

    data object ServerError : WeatherError

    data class Unknown(
        val cause: Throwable? = null,
    ) : WeatherError
}

fun WeatherError.toMessage(): String =
    when (this) {
        WeatherError.NoInternet ->
            "No internet connection"

        WeatherError.Unauthorized ->
            "Request was not authorized"

        WeatherError.LocationUnavailable ->
            "Current location is unavailable, make sure to grant permission and enable GPS"

        WeatherError.ServerError ->
            "Weather service is temporarily unavailable"

        is WeatherError.Unknown ->
            "Something went wrong"
    }
