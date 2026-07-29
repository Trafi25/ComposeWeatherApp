package com.plcoding.weatherapp.presentation.weather

import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.settings.*

sealed interface WeatherAction {
    data object LoadWeather : WeatherAction
    data object Retry : WeatherAction
    data object RequestLocationPermission : WeatherAction
    data object LocationPermissionDenied : WeatherAction
    data object LocationPermissionGranted : WeatherAction
    data object CurrentLocationSelected : WeatherAction
    data object ErrorDismissed : WeatherAction

    // Navigation
    data object SearchCityClicked : WeatherAction
    data object ManageCitiesClicked : WeatherAction
    data object SettingsClicked : WeatherAction
    data object CityScreenBackClicked : WeatherAction
    data object SettingsBackClicked : WeatherAction

    // Settings
    data object ClearCacheClicked : WeatherAction
    data class TemperatureUnitSelected(val unit: TemperatureUnit) : WeatherAction
    data class WindSpeedUnitSelected(val unit: WindSpeedUnit) : WeatherAction
    data class PressureUnitSelected(val unit: PressureUnit) : WeatherAction
    data class PrecipitationUnitSelected(val unit: PrecipitationUnit) : WeatherAction
    data class TimeFormatSelected(val format: AppTimeFormat) : WeatherAction
    data class ThemeModeSelected(val mode: AppThemeMode) : WeatherAction
    data class AccentColorSelected(val color: AppAccentColor) : WeatherAction

    data class SearchQueryChanged(val query: String) : WeatherAction
    data class CitySelected(val city: City) : WeatherAction
    data class SavedCityDeleted(val cityId: Int) : WeatherAction
}
