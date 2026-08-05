package com.plcoding.weatherapp.presentation.weather.current

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.plcoding.weatherapp.presentation.ui.theme.spacing
import com.plcoding.weatherapp.presentation.weather.WeatherAction
import com.plcoding.weatherapp.presentation.weather.common.WeatherErrorContent
import com.plcoding.weatherapp.presentation.weather.daily.SevenDayForecast
import com.plcoding.weatherapp.presentation.weather.hourly.WeatherForecast
import com.plcoding.weatherapp.presentation.weather.state.WeatherState

@Composable
fun WeatherContent(
    uiState: WeatherState,
    onAction: (WeatherAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = {
            onAction(WeatherAction.CurrentLocationSelected)
        },
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .statusBarsPadding(),
        ) {
            WeatherTopBar(
                locationName = uiState.locationName,
                onSearchClick = {
                    onAction(WeatherAction.SearchCityClicked)
                },
                onManageCitiesClick = {
                    onAction(WeatherAction.ManageCitiesClicked)
                },
                onSettingsClick = { onAction(WeatherAction.SettingsClicked) },
            )

            Box(
                modifier =
                    Modifier
                        .fillMaxSize(),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = MaterialTheme.spacing.large),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                ) {
                    item {
                        WeatherCard(
                            state = uiState,
                            locationName = uiState.locationName,
                            backgroundColor = MaterialTheme.colorScheme.surface,
                        )
                    }

                    item {
                        WeatherForecast(
                            state = uiState,
                        )
                    }

                    item {
                        SevenDayForecast(
                            dailyWeather =
                                uiState.weatherInfo
                                    ?.dailyWeatherData
                                    .orEmpty(),
                        )
                    }
                }

                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                uiState.errorMessage?.let { error ->
                    WeatherErrorContent(
                        message = error,
                        onRetry = {
                            onAction(WeatherAction.Retry)
                        },
                        onDismiss = {
                            onAction(WeatherAction.ErrorDismissed)
                        },
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }
    }
}
