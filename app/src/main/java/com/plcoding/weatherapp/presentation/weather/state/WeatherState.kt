package com.plcoding.weatherapp.presentation.weather.state

import androidx.compose.runtime.Immutable
import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.domain.weather.WeatherInfo

/**
 * Represents the complete UI state of the weather screen.
 *
 * This state contains the currently loaded weather data, loading and error information,
 * the resolved location name, the currently displayed screen mode, and the state of the
 * AI weather recommendation.
 *
 * The class is marked as [Immutable] so that Jetpack Compose can treat its instances
 * as immutable and avoid unnecessary recompositions when the state has not changed.
 *
 * @property weatherInfo Weather information for the currently selected location.
 * `null` when weather data has not been loaded yet.
 *
 * @property isLoading Indicates whether weather data or other screen content is currently loading.
 *
 * @property errorMessage A user-readable error message.
 * `null` when there is no active error.
 *
 * @property locationName The display name of the current location, such as `"Nuremberg"`.
 * `null` when the location name has not been resolved yet.
 *
 * @property screenMode Determines which main screen content is currently displayed,
 * such as the weather screen, city search, or city management.
 *
 * @property selectedCityId The ID of the currently selected city from the user's saved cities.
 * `null` if the user's current GPS location is being used.
 *
 * @property appSettings User preferences for units, time format, and theme.
 *
 * @property aiSummary A generated natural-language weather summary and recommendation.
 *
 * @property isAiLoading Indicates whether the AI summary is currently being generated.
 *
 * @property aiErrorMessage Error message if the AI summary generation fails.
 *
 * @property lastUpdated Timestamp of the last successful weather data fetch.
 */
@Immutable
data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val locationName: String? = null,
    val screenMode: WeatherScreenMode = WeatherScreenMode.Weather,
    val selectedCityId: Int? = null,
    val appSettings: AppSettings = AppSettings(),
    val aiSummary: String? = null,
    val isAiLoading: Boolean = false,
    val aiErrorMessage: String? = null,
    val lastUpdated: Long = 0L,
)
