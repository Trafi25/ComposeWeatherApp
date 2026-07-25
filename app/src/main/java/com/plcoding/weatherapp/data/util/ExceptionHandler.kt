package com.plcoding.weatherapp.data.util

import com.plcoding.weatherapp.domain.util.DataError
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Exception.toWeatherError(): DataError =
    when (this) {
        is UnknownHostException,
        is ConnectException,
        -> DataError.NoInternet
        is SocketTimeoutException ->
            DataError.ServerError
        is HttpException -> {
            when (code()) {
                401, 403 -> DataError.Unauthorized
                in 500..599 -> DataError.ServerError
                else -> DataError.Unknown(this)
            }
        }
        else -> DataError.Unknown(this)
    }
