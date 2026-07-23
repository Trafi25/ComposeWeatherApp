package com.plcoding.weatherapp.data.remote

import com.plcoding.weatherapp.data.remote.dtos.GeocodingResponseDto
import retrofit2.http.GET
import retrofit2.http.Query


interface GeoCodingApi {
    @GET("v1/forecast")
    suspend fun searchCities(
        @Query("name") query: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json"
    ): GeocodingResponseDto
}
