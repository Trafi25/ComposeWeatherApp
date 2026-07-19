package com.plcoding.weatherapp.data.remote

import com.plcoding.weatherapp.data.remote.WeatherQueryFields.CURRENT
import com.plcoding.weatherapp.data.remote.WeatherQueryFields.HOURLY
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("current") current: String = CURRENT,
        @Query("hourly") hourly: String = HOURLY,
        @Query("timezone")
        timezone: String = "auto",
    ): WeatherDto
}
