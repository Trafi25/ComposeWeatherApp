package com.plcoding.weatherapp.data.util

import com.plcoding.weatherapp.domain.util.WeatherError
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Exception.toWeatherError(): WeatherError {
    return when (this) {
        is UnknownHostException,
        is ConnectException
            -> WeatherError.NoInternet
        is SocketTimeoutException ->
            WeatherError.ServerError
        is HttpException -> {
            when (code()) {
                401, 403 -> WeatherError.Unauthorized
                in 500..599 -> WeatherError.ServerError
                else -> WeatherError.Unknown(this)
            }
        }
        else -> WeatherError.Unknown(this)
    }
}
