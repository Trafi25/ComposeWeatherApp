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
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.presentation.weather.city.CityManagerAction
import com.plcoding.weatherapp.presentation.weather.city.CityManagerScreen
import com.plcoding.weatherapp.presentation.weather.city.CitySearchAction
import com.plcoding.weatherapp.presentation.weather.city.CitySearchScreen
import com.plcoding.weatherapp.presentation.weather.common.WeatherSystemBar
import com.plcoding.weatherapp.presentation.weather.current.WeatherContent
import com.plcoding.weatherapp.presentation.weather.settings.SettingsAction
import com.plcoding.weatherapp.presentation.weather.settings.SettingsScreen
import com.plcoding.weatherapp.presentation.weather.state.CitySearchState
import com.plcoding.weatherapp.presentation.weather.state.WeatherScreenMode
import com.plcoding.weatherapp.presentation.weather.state.WeatherState

@Composable
fun WeatherMainScreen(
    windowSizeClass: WindowSizeClass,
    weatherState: WeatherState,
    searchState: CitySearchState,
    settingsState: AppSettings,
    savedCities: List<City>,
    selectedCityId: Int?,
    onWeatherAction: (WeatherAction) -> Unit,
    onSearchAction: (CitySearchAction) -> Unit,
    onSettingsAction: (SettingsAction) -> Unit,
    onManagerAction: (CityManagerAction) -> Unit,
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
            targetState = weatherState.screenMode,
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
                        windowSizeClass = windowSizeClass,
                        uiState = weatherState.copy(appSettings = settingsState),
                        onAction = onWeatherAction,
                    )
                }
                WeatherScreenMode.SearchCity -> {
                    CitySearchScreen(
                        state = searchState,
                        onAction = onSearchAction,
                    )
                }
                WeatherScreenMode.ManageCities -> {
                    CityManagerScreen(
                        cities = savedCities,
                        selectedCityId = selectedCityId,
                        onAction = onManagerAction,
                    )
                }
                WeatherScreenMode.Settings -> {
                    SettingsScreen(
                        settings = settingsState,
                        onAction = onSettingsAction,
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
