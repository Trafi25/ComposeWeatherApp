package com.plcoding.weatherapp.presentation.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.presentation.ui.theme.DayBackground
import com.plcoding.weatherapp.presentation.ui.theme.DayCardBackground
import com.plcoding.weatherapp.presentation.ui.theme.NightBackground
import com.plcoding.weatherapp.presentation.ui.theme.NightCardBackground
import com.plcoding.weatherapp.presentation.weather.components.WeatherCard
import com.plcoding.weatherapp.presentation.weather.components.WeatherErrorContent
import com.plcoding.weatherapp.presentation.weather.components.WeatherForeCast
import com.plcoding.weatherapp.presentation.weather.components.WeatherSystemBar

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
                .statusBarsPadding(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
        ) {
            WeatherCard(
                state = uiState,
                locationName = uiState.locationName,
                backgroundColor = cardBackground,
            )
            Spacer(modifier = Modifier.height(16.dp))
            WeatherForeCast(state = uiState)
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
