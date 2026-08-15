package com.plcoding.weatherapp.presentation.weather

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import com.plcoding.weatherapp.presentation.weather.city.CityManagerScreen
import com.plcoding.weatherapp.presentation.weather.city.CitySearchScreen
import com.plcoding.weatherapp.presentation.weather.common.WeatherSystemBar
import com.plcoding.weatherapp.presentation.weather.current.WeatherContent
import com.plcoding.weatherapp.presentation.weather.settings.SettingsScreen
import com.plcoding.weatherapp.presentation.weather.state.WeatherScreenMode
import com.plcoding.weatherapp.presentation.weather.state.WeatherState

@Composable
fun WeatherMainScreen(
    uiState: WeatherState,
    onAction: (WeatherAction) -> Unit,
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    val isDarkTheme = backgroundColor.red + backgroundColor.green + backgroundColor.blue < 1.5f

    WeatherSystemBar(
        backgroundColor = backgroundColor,
        useDarkIcons = !isDarkTheme,
    )

    val springSpec =
        spring<IntOffset>(
            dampingRatio = 0.75f,
            stiffness = Spring.StiffnessLow,
        )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(backgroundColor),
    ) {
        AnimatedContent(
            targetState = uiState.screenMode,
            transitionSpec =
                {
                    val direction =
                        if (targetState.toOrder() > initialState.toOrder()) {
                            AnimatedContentTransitionScope.SlideDirection.Left
                        } else {
                            AnimatedContentTransitionScope.SlideDirection.Right
                        }
                    slideIntoContainer(
                        towards = direction,
                        animationSpec = springSpec,
                    ) + fadeIn() togetherWith
                        slideOutOfContainer(
                            towards = direction,
                            animationSpec = springSpec,
                        ) + fadeOut()
                },
            label = "Weather screen navigation",
        ) { screenMode ->
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
                            onAction(WeatherAction.ToggleTemperatureUnitRequested)
                        },
                        onWindSpeedUnitClick = {
                            onAction(WeatherAction.ToggleWindSpeedUnitRequested)
                        },
                        onPressureUnitClick = {
                            onAction(WeatherAction.TogglePressureUnitRequested)
                        },
                        onPrecipitationUnitClick = {
                            onAction(WeatherAction.TogglePrecipitationUnitRequested)
                        },
                        onTimeFormatClick = {
                            onAction(WeatherAction.ToggleTimeFormatRequested)
                        },
                        onThemeModeClick = {
                            onAction(WeatherAction.ToggleThemeModeRequested)
                        },
                        onAccentColorSelected = { color ->
                            onAction(WeatherAction.AccentColorSelected(color))
                        },
                        onClearCacheClick = {
                            onAction(WeatherAction.ClearCacheClicked)
                        },
                        onBackClick = {
                            onAction(WeatherAction.SettingsBackClicked)
                        },
                        onNotificationToggle = {
                            onAction(
                                WeatherAction.NotificationToggleClicked(
                                    !uiState.appSettings.weatherNotificationEnabled,
                                ),
                            )
                        },
                    )
                }
            }
        }
    }
}

private fun WeatherScreenMode.toOrder(): Int =
    when (this) {
        WeatherScreenMode.Weather -> 0
        WeatherScreenMode.ManageCities -> 1
        WeatherScreenMode.SearchCity -> 2
        WeatherScreenMode.Settings -> 3
    }
