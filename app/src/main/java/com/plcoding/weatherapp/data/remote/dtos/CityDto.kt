package com.plcoding.weatherapp.data.remote.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityDto(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String?,
    @Json(name = "admin1")
    val adminArea: String?,
    val timezone: String?,
)
