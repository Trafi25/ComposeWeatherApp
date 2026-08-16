package com.plcoding.weatherapp.data.remote.dtos.cities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class GeocodingResponseDto(
    val results: List<CityDto>?,
)
