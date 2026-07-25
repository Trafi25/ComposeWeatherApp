package com.plcoding.weatherapp.domain.util

sealed interface DataError {
    data object NoInternet : DataError

    data object Unauthorized : DataError

    data object LocationUnavailable : DataError

    data object ServerError : DataError

    data object InvalidQuery : DataError

    data class Unknown(
        val cause: Throwable? = null,
    ) : DataError
}

fun DataError.toMessage(): String =
    when (this) {
        DataError.NoInternet ->
            "No internet connection"

        DataError.Unauthorized ->
            "Request was not authorized"

        DataError.LocationUnavailable ->
            "Current location is unavailable, make sure to grant permission and enable GPS"

        DataError.ServerError ->
            "Weather service is temporarily unavailable"

        DataError.InvalidQuery ->
            "Enter at least two characters"

        is DataError.Unknown ->
            "Something went wrong"
    }
