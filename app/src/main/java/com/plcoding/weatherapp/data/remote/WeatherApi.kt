package com.plcoding.weatherapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("current") current: String =
            "temperature_2m,relative_humidity_2m,weather_code," +
                "pressure_msl,wind_speed_10m,is_day",
        @Query("hourly") hourly: String =
            "temperature_2m,relative_humidity_2m,weather_code," +
                "pressure_msl,wind_speed_10m,is_day",
        @Query("timezone")
        timezone: String = "auto",
    ): WeatherDto
}
