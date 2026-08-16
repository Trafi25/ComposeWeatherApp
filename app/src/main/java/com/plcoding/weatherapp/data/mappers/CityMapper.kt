package com.plcoding.weatherapp.data.mappers

import com.plcoding.weatherapp.data.remote.dtos.cities.CityDto
import com.plcoding.weatherapp.domain.location.City

internal fun CityDto.toCityDomain(): City =
    City(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country.orEmpty(),
        adminArea = adminArea,
        timezone = timezone.orEmpty(),
    )
