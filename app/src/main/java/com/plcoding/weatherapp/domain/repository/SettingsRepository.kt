package com.plcoding.weatherapp.domain.repository

import com.plcoding.weatherapp.domain.settings.AppAccentColor
import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.domain.settings.AppThemeMode
import com.plcoding.weatherapp.domain.settings.AppTimeFormat
import com.plcoding.weatherapp.domain.settings.PrecipitationUnit
import com.plcoding.weatherapp.domain.settings.PressureUnit
import com.plcoding.weatherapp.domain.settings.TemperatureUnit
import com.plcoding.weatherapp.domain.settings.WindSpeedUnit
import kotlinx.coroutines.flow.Flow

interface SettingsRepository
{

    fun observeSettings(): Flow<AppSettings>

    suspend fun setTemperatureUnit(
        unit: TemperatureUnit,
    )


    suspend fun setWindSpeedUnit(
        unit: WindSpeedUnit,
    )

    suspend fun setPressureUnit(
        unit: PressureUnit,
    )

    suspend fun setPrecipitationUnit(
        unit: PrecipitationUnit,
    )

    suspend fun setTimeFormat(
        format: AppTimeFormat,
    )

    suspend fun setThemeMode(
        mode: AppThemeMode,
    )

    suspend fun setAccentColor(
        color: AppAccentColor,
    )



}
