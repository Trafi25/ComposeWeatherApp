package com.plcoding.weatherapp.data.mappers

import com.plcoding.weatherapp.data.remote.dtos.CityDto
import com.plcoding.weatherapp.domain.location.City

fun CityDto.toCityDomain() : City =
    City(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        adminArea = adminArea,
        timezone = timezone
    )
