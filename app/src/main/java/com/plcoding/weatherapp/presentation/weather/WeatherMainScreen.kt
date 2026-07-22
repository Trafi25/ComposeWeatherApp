package com.plcoding.weatherapp.presentation.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.presentation.ui.theme.DayBackground
import com.plcoding.weatherapp.presentation.ui.theme.DayCardBackground
import com.plcoding.weatherapp.presentation.ui.theme.NightBackground
import com.plcoding.weatherapp.presentation.ui.theme.NightCardBackground
import com.plcoding.weatherapp.presentation.weather.common.WeatherErrorContent
import com.plcoding.weatherapp.presentation.weather.common.WeatherSystemBar
import com.plcoding.weatherapp.presentation.weather.curent.WeatherCard
import com.plcoding.weatherapp.presentation.weather.daily.SevenDayForecast
import com.plcoding.weatherapp.presentation.weather.hourly.WeatherForecast

@Composable
fun WeatherMainScreen(
    uiState: WeatherState,
    onAction: (WeatherAction) -> Unit,
) {
    val isDay = uiState.weatherInfo?.currentWeatherData?.isDay ?: true
    val backgroundColor = if (isDay) DayBackground else NightBackground
    val cardBackground = if (isDay) DayCardBackground else NightCardBackground
    WeatherSystemBar(
        backgroundColor = backgroundColor,
        useDarkIcons = isDay,
    )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(backgroundColor),
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                WeatherCard(
                    state = uiState,
                    locationName = uiState.locationName,
                    backgroundColor = cardBackground,
                )
            }
            item {
                WeatherForecast(
                    state = uiState,
                )
            }
            item {
                SevenDayForecast(dailyWeather = uiState.weatherInfo?.dailyWeatherData.orEmpty())
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
