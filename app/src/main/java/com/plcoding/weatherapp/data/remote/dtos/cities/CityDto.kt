package com.plcoding.weatherapp.data.remote.dtos.cities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class CityDto(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null,
    @Json(name = "admin1")
    val adminArea: String? = null,
    val timezone: String? = null,
)
