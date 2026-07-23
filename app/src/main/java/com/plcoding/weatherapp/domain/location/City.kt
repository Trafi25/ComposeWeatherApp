package com.plcoding.weatherapp.domain.location

data class City(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String?,
    val adminArea: String?,
    val timezone: String?,
)
