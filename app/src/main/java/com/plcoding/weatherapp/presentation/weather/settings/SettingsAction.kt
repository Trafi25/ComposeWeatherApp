package com.plcoding.weatherapp.presentation.weather.settings

import com.plcoding.weatherapp.domain.settings.AppAccentColor

sealed interface SettingsAction {
    data object ToggleTemperatureUnit : SettingsAction

    data object ToggleWindSpeedUnit : SettingsAction

    data object TogglePressureUnit : SettingsAction

    data object TogglePrecipitationUnit : SettingsAction

    data object ToggleTimeFormat : SettingsAction

    data object ToggleThemeMode : SettingsAction

    data class AccentColorSelected(
        val color: AppAccentColor,
    ) : SettingsAction

    data object ToggleNotifications : SettingsAction

    data object ClearCache : SettingsAction

    data object BackClicked : SettingsAction
}
