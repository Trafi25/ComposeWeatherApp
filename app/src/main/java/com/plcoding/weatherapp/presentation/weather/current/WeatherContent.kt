package com.plcoding.weatherapp.presentation.weather.current

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.presentation.ui.theme.spacing
import com.plcoding.weatherapp.presentation.weather.WeatherAction
import com.plcoding.weatherapp.presentation.weather.common.WeatherErrorContent
import com.plcoding.weatherapp.presentation.weather.daily.SevenDayForecast
import com.plcoding.weatherapp.presentation.weather.hourly.WeatherForecast
import com.plcoding.weatherapp.presentation.weather.state.WeatherState

@Composable
fun WeatherContent(
    windowSizeClass: WindowSizeClass,
    uiState: WeatherState,
    onAction: (WeatherAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    PullToRefreshBox(
        isRefreshing = uiState.isLoading && uiState.weatherInfo != null,
        onRefresh = {
            onAction(WeatherAction.Refresh)
        },
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.widthIn(max = 1200.dp),
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
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
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

                            item {
                                if (uiState.aiSummary != null || uiState.isAiLoading || uiState.aiErrorMessage != null) {
                                    AiWeatherCard(
                                        summary = uiState.aiSummary,
                                        isLoading = uiState.isAiLoading,
                                        errorMessage = uiState.aiErrorMessage,
                                    )
                                }
                            }
                        }
                    } else {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = MaterialTheme.spacing.large),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                        ) {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(bottom = MaterialTheme.spacing.large),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                            ) {
                                item {
                                    WeatherCard(
                                        state = uiState,
                                        locationName = uiState.locationName,
                                        backgroundColor = MaterialTheme.colorScheme.surface,
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }
                                item {
                                    if (uiState.aiSummary != null || uiState.isAiLoading || uiState.aiErrorMessage != null) {
                                        AiWeatherCard(
                                            summary = uiState.aiSummary,
                                            isLoading = uiState.isAiLoading,
                                            errorMessage = uiState.aiErrorMessage,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
                                }
                            }
                            LazyColumn(
                                modifier = Modifier.weight(1.2f),
                                contentPadding = PaddingValues(bottom = MaterialTheme.spacing.large),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                            ) {
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
                        }
                    }

                    if (uiState.isLoading && uiState.weatherInfo == null) {
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
}
