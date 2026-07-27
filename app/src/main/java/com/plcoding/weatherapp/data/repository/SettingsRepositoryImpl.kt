package com.plcoding.weatherapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.plcoding.weatherapp.data.preferences.settingsDataStore
import com.plcoding.weatherapp.domain.repository.SettingsRepository
import com.plcoding.weatherapp.domain.settings.AppAccentColor
import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.domain.settings.AppThemeMode
import com.plcoding.weatherapp.domain.settings.AppTimeFormat
import com.plcoding.weatherapp.domain.settings.PrecipitationUnit
import com.plcoding.weatherapp.domain.settings.PressureUnit
import com.plcoding.weatherapp.domain.settings.TemperatureUnit
import com.plcoding.weatherapp.domain.settings.WindSpeedUnit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.EncodeDefault
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): SettingsRepository {

    private val TEMPERATURE_UNIT =
        stringPreferencesKey("temperature_unit")

    private val WIND_SPEED_UNIT =
        stringPreferencesKey("wind_speed_unit")

    private val PRESSURE_UNIT =
        stringPreferencesKey("pressure_unit")

    private val PRECIPITATION_UNIT =
        stringPreferencesKey("precipitation_unit")

    private val TIME_FORMAT =
        stringPreferencesKey("time_format")

    private val THEME_MODE =
        stringPreferencesKey("theme_mode")

    private val ACCENT_COLOR =
        stringPreferencesKey("accent_color")


    override fun observeSettings(): Flow<AppSettings> {
        return context.settingsDataStore.data.map {
            preferences ->
            AppSettings(
                temperatureUnit = preferences[TEMPERATURE_UNIT]
                .toEnumOrDefault(default =  TemperatureUnit.Celsius),
                windSpeedUnit = preferences[WIND_SPEED_UNIT]
                    .toEnumOrDefault(default = WindSpeedUnit.KilometersPerHour),
                pressureUnit  = preferences[PRESSURE_UNIT]
                    .toEnumOrDefault(default = PressureUnit.Hectopascal),
                precipitationUnit  = preferences[PRECIPITATION_UNIT]
                    .toEnumOrDefault(default = PrecipitationUnit.Millimeter),
                timeFormat = preferences[TIME_FORMAT]
                    .toEnumOrDefault(default = AppTimeFormat.SystemDefault,),
                themeMode =preferences[THEME_MODE]
                    .toEnumOrDefault(default =AppThemeMode.SystemDefault,),
                accentColor =preferences[ACCENT_COLOR]
                    .toEnumOrDefault(default = AppAccentColor.Green,),)
        }
    }

    override suspend fun setTemperatureUnit(unit: TemperatureUnit) {
        context.settingsDataStore.edit { preferences ->
            preferences[TEMPERATURE_UNIT] = unit.name }
    }

    override suspend fun setWindSpeedUnit(unit: WindSpeedUnit) {
        context.settingsDataStore.edit { preferences ->
            preferences[WIND_SPEED_UNIT] = unit.name
        }
    }

    override suspend fun setPressureUnit(unit: PressureUnit) {
        context.settingsDataStore.edit { preferences ->
            preferences[PRESSURE_UNIT] = unit.name
        }
    }

    override suspend fun setPrecipitationUnit(unit: PrecipitationUnit) {
        context.settingsDataStore.edit { preferences ->
            preferences[PRECIPITATION_UNIT] = unit.name
        }
    }

    override suspend fun setTimeFormat(format: AppTimeFormat) {
        context.settingsDataStore.edit { preferences ->
            preferences[TIME_FORMAT] = format.name
        }
    }

    override suspend fun setThemeMode(mode: AppThemeMode) {
        context.settingsDataStore.edit { preferences ->
            preferences[THEME_MODE] = mode.name
        }
    }

    override suspend fun setAccentColor(color: AppAccentColor) {
        context.settingsDataStore.edit { preferences ->
            preferences[ACCENT_COLOR] = color.name
        }
    }

    private inline fun <reified T : Enum<T>> String?.toEnumOrDefault(
        default: T
    ): T{
        return enumValues<T>().firstOrNull{
            enumValue -> enumValue.name == this
        } ?: default
    }



}
