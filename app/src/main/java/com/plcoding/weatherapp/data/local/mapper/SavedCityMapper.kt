package com.plcoding.weatherapp.data.local.mapper

import com.plcoding.weatherapp.data.local.SavedCityEntity
import com.plcoding.weatherapp.domain.location.City

fun City.toEntity(): SavedCityEntity =
    SavedCityEntity(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        adminArea = adminArea,
        timezone = timezone,
    )

fun SavedCityEntity.toDomain(): City =
    City(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        adminArea = adminArea,
        timezone = timezone,
    )
