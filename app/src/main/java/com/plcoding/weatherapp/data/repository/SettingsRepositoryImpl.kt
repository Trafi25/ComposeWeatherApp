package com.plcoding.weatherapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
import javax.inject.Inject

class SettingsRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : SettingsRepository {
        override fun observeSettings(): Flow<AppSettings> =
            context.settingsDataStore.data.map { preferences ->
                AppSettings(
                    temperatureUnit =
                        preferences[TEMPERATURE_UNIT_KEY]
                            .toEnumOrDefault(default = TemperatureUnit.Celsius),
                    windSpeedUnit =
                        preferences[WIND_SPEED_UNIT_KEY]
                            .toEnumOrDefault(default = WindSpeedUnit.KilometersPerHour),
                    pressureUnit =
                        preferences[PRESSURE_UNIT_KEY]
                            .toEnumOrDefault(default = PressureUnit.Hectopascal),
                    precipitationUnit =
                        preferences[PRECIPITATION_UNIT_KEY]
                            .toEnumOrDefault(default = PrecipitationUnit.Millimeter),
                    timeFormat =
                        preferences[TIME_FORMAT_KEY]
                            .toEnumOrDefault(default = AppTimeFormat.SystemDefault),
                    themeMode =
                        preferences[THEME_MODE_KEY]
                            .toEnumOrDefault(default = AppThemeMode.SystemDefault),
                    accentColor =
                        preferences[ACCENT_COLOR_KEY]
                            .toEnumOrDefault(default = AppAccentColor.Green),
                    weatherNotificationEnabled = preferences[NOTIFICATION_KEY] ?: false,
                )
            }

        override suspend fun setTemperatureUnit(unit: TemperatureUnit) {
            context.settingsDataStore.edit { preferences ->
                preferences[TEMPERATURE_UNIT_KEY] = unit.name
            }
        }

        override suspend fun setWindSpeedUnit(unit: WindSpeedUnit) {
            context.settingsDataStore.edit { preferences ->
                preferences[WIND_SPEED_UNIT_KEY] = unit.name
            }
        }

        override suspend fun setPressureUnit(unit: PressureUnit) {
            context.settingsDataStore.edit { preferences ->
                preferences[PRESSURE_UNIT_KEY] = unit.name
            }
        }

        override suspend fun setPrecipitationUnit(unit: PrecipitationUnit) {
            context.settingsDataStore.edit { preferences ->
                preferences[PRECIPITATION_UNIT_KEY] = unit.name
            }
        }

        override suspend fun setTimeFormat(format: AppTimeFormat) {
            context.settingsDataStore.edit { preferences ->
                preferences[TIME_FORMAT_KEY] = format.name
            }
        }

        override suspend fun setThemeMode(mode: AppThemeMode) {
            context.settingsDataStore.edit { preferences ->
                preferences[THEME_MODE_KEY] = mode.name
            }
        }

        override suspend fun setAccentColor(color: AppAccentColor) {
            context.settingsDataStore.edit { preferences ->
                preferences[ACCENT_COLOR_KEY] = color.name
            }
        }

        override suspend fun setNotificationEnabled(isEnabled: Boolean) {
            context.settingsDataStore.edit { preferences ->
                preferences[NOTIFICATION_KEY] = isEnabled
            }
        }

        private inline fun <reified T : Enum<T>> String?.toEnumOrDefault(default: T): T =
            enumValues<T>().firstOrNull { enumValue ->
                enumValue.name == this
            } ?: default

        private companion object {
            val TEMPERATURE_UNIT_KEY = stringPreferencesKey("temperature_unit")
            val WIND_SPEED_UNIT_KEY = stringPreferencesKey("wind_speed_unit")
            val PRESSURE_UNIT_KEY = stringPreferencesKey("pressure_unit")
            val PRECIPITATION_UNIT_KEY = stringPreferencesKey("precipitation_unit")
            val TIME_FORMAT_KEY = stringPreferencesKey("time_format")
            val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
            val ACCENT_COLOR_KEY = stringPreferencesKey("accent_color")
            val NOTIFICATION_KEY = booleanPreferencesKey("notification_enabled")
        }
    }
