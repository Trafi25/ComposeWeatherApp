package com.plcoding.weatherapp.presentation.weather

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import com.plcoding.weatherapp.domain.settings.AppTimeFormat
import com.plcoding.weatherapp.domain.settings.PrecipitationUnit
import com.plcoding.weatherapp.domain.settings.PressureUnit
import com.plcoding.weatherapp.domain.settings.TemperatureUnit
import com.plcoding.weatherapp.domain.settings.WindSpeedUnit
import com.plcoding.weatherapp.presentation.ui.theme.DayBackground
import com.plcoding.weatherapp.presentation.ui.theme.NightBackground
import com.plcoding.weatherapp.presentation.weather.city.CityManagerScreen
import com.plcoding.weatherapp.presentation.weather.city.CitySearchScreen
import com.plcoding.weatherapp.presentation.weather.curent.WeatherContent
import com.plcoding.weatherapp.presentation.weather.settings.SettingsScreen
import com.plcoding.weatherapp.presentation.weather.states.WeatherScreenMode
import com.plcoding.weatherapp.presentation.weather.states.WeatherState

@Composable
fun WeatherMainScreen(
    uiState: WeatherState,
    onAction: (WeatherAction) -> Unit,
) {
    AnimatedContent(
        targetState = uiState.screenMode,
        transitionSpec =
            {
                when {
                    targetState == WeatherScreenMode.SearchCity || targetState == WeatherScreenMode.Settings -> {
                        slideInHorizontally { width -> width } +
                            fadeIn() togetherWith
                            slideOutHorizontally { width -> -width / 3 } +
                            fadeOut()
                    }
                    initialState == WeatherScreenMode.SearchCity || initialState == WeatherScreenMode.Settings -> {
                        slideInHorizontally { width -> -width / 3 } +
                            fadeIn() togetherWith
                            slideOutHorizontally { width -> width } +
                            fadeOut()
                    }
                    else -> fadeIn() togetherWith fadeOut()
                }
            },
        label = "Weather screen navigation",
    ) { screenMode ->
        val isDay = uiState.weatherInfo?.currentWeatherData?.isDay ?: true
        val backgroundColor = if (isDay) DayBackground else NightBackground
        when (screenMode) {
            WeatherScreenMode.Weather -> {
                WeatherContent(
                    uiState = uiState,
                    onAction = onAction,
                )
            }
            WeatherScreenMode.SearchCity -> {
                CitySearchScreen(
                    state = uiState.citySearchState,
                    backgroundColor = backgroundColor,
                    onQueryChanged = { query ->
                        onAction(WeatherAction.SearchQueryChanged(query))
                    },
                    onCityClick = { city ->
                        onAction(WeatherAction.CitySelected(city))
                    },
                    onBackClick = {
                        onAction(WeatherAction.CityScreenBackClicked)
                    },
                )
            }
            WeatherScreenMode.ManageCities -> {
                CityManagerScreen(
                    cities = uiState.savedCities,
                    backgroundColor = backgroundColor,
                    selectedCityId = uiState.selectedCityId,
                    onCityClick = { city ->
                        onAction(
                            WeatherAction.CitySelected(city),
                        )
                    },
                    onCityDelete = { city ->
                        onAction(
                            WeatherAction.SavedCityDeleted(city.id),
                        )
                    },
                    onAddCityClick = {
                        onAction(WeatherAction.SearchCityClicked)
                    },
                    onBackClick = { onAction(WeatherAction.CityScreenBackClicked) },
                    onCurrentLocationClick = {
                        onAction(
                            WeatherAction.CurrentLocationSelected,
                        )
                    },
                )
            }
            WeatherScreenMode.Settings -> {
                SettingsScreen(
                    settings = uiState.appSettings,
                    onTemperatureUnitClick = {
                        val newUnit = if (uiState.appSettings.temperatureUnit == TemperatureUnit.Celsius) {
                            TemperatureUnit.Fahrenheit
                        } else {
                            TemperatureUnit.Celsius
                        }
                        onAction(WeatherAction.TemperatureUnitSelected(newUnit))
                    },
                    onWindSpeedUnitClick = {
                        val units = WindSpeedUnit.entries
                        val currentIndex = units.indexOf(uiState.appSettings.windSpeedUnit)
                        val nextIndex = (currentIndex + 1) % units.size
                        onAction(WeatherAction.WindSpeedUnitSelected(units[nextIndex]))
                    },
                    onPressureUnitClick = {
                        val units = PressureUnit.entries
                        val currentIndex = units.indexOf(uiState.appSettings.pressureUnit)
                        val nextIndex = (currentIndex + 1) % units.size
                        onAction(WeatherAction.PressureUnitSelected(units[nextIndex]))
                    },
                    onPrecipitationUnitClick = {
                        val units = PrecipitationUnit.entries
                        val currentIndex = units.indexOf(uiState.appSettings.precipitationUnit)
                        val nextIndex = (currentIndex + 1) % units.size
                        onAction(WeatherAction.PrecipitationUnitSelected(units[nextIndex]))
                    },
                    onTimeFormatClick = {
                        val formats = AppTimeFormat.entries
                        val currentIndex = formats.indexOf(uiState.appSettings.timeFormat)
                        val nextIndex = (currentIndex + 1) % formats.size
                        onAction(WeatherAction.TimeFormatSelected(formats[nextIndex]))
                    },
                    onClearCacheClick = {
                        onAction(WeatherAction.ClearCacheClicked)
                    },
                    onBackClick = {
                        onAction(WeatherAction.SettingsBackClicked)
                    },
                    backgroundColor = backgroundColor,
                )
            }
        }
    }
}
