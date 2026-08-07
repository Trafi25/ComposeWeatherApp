package com.plcoding.weatherapp.presentation.weather

import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.settings.*

/**
 * Represents all possible user actions and lifecycle events on the Weather screen.
 *
 */
sealed interface WeatherAction {

    // --- Lifecycle & System Actions ---

    /** Triggered when the screen first opens to restore previous state. */
    data object LoadWeather : WeatherAction

    /** Retries a failed weather fetch. */
    data object Retry : WeatherAction

    /** Called when the UI needs to trigger the system permission dialog. */
    data object RequestLocationPermission : WeatherAction

    /** Called when the user permanently denies location access. */
    data object LocationPermissionDenied : WeatherAction

    /** Called when location access is finally granted by the user. */
    data object LocationPermissionGranted : WeatherAction

    /** Manually switches back to GPS-based weather from a saved city. */
    data object CurrentLocationSelected : WeatherAction

    /** Refreshes weather for the current selection. */
    data object Refresh : WeatherAction

    /** Clears the current error message from the screen. */
    data object ErrorDismissed : WeatherAction

    // --- Navigation Actions ---

    /** Switches screen mode to City Search. */
    data object SearchCityClicked : WeatherAction

    /** Switches screen mode to City Management. */
    data object ManageCitiesClicked : WeatherAction

    /** Switches screen mode to Settings. */
    data object SettingsClicked : WeatherAction

    /** Navigates back from Search or Management to the main Weather view. */
    data object CityScreenBackClicked : WeatherAction

    /** Navigates back from Settings to the main Weather view. */
    data object SettingsBackClicked : WeatherAction

    // --- User Intent Actions ---

    /** Triggered as the user types in the search bar. */
    data class SearchQueryChanged(
        val query: String,
    ) : WeatherAction

    /** Called when a user clicks a city from search results or the saved list. */
    data class CitySelected(
        val city: City,
    ) : WeatherAction

    /** Triggered when a user swipes-to-delete or clicks delete on a saved city. */
    data class SavedCityDeleted(
        val cityId: Int,
    ) : WeatherAction

    // --- Settings Update Actions ---

    /** Deletes all offline data from the Room database. */
    data object ClearCacheClicked : WeatherAction

    data class TemperatureUnitSelected(
        val unit: TemperatureUnit,
    ) : WeatherAction

    data class WindSpeedUnitSelected(
        val unit: WindSpeedUnit,
    ) : WeatherAction

    data class PressureUnitSelected(
        val unit: PressureUnit,
    ) : WeatherAction

    data class PrecipitationUnitSelected(
        val unit: PrecipitationUnit,
    ) : WeatherAction

    data class TimeFormatSelected(
        val format: AppTimeFormat,
    ) : WeatherAction

    data class ThemeModeSelected(
        val mode: AppThemeMode,
    ) : WeatherAction

    data class AccentColorSelected(
        val color: AppAccentColor,
    ) : WeatherAction
}
