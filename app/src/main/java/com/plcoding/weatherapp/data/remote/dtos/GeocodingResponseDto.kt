package com.plcoding.weatherapp.data.remote.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingResponseDto(
    val results: List<CityDto>?
)
