package com.plcoding.weatherapp.presentation.weather.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.weatherapp.domain.weather.upcomingHours
import com.plcoding.weatherapp.presentation.weather.WeatherState

@Composable
fun WeatherForecast(
    state: WeatherState,
    modifier: Modifier = Modifier,
) {
    val upcomingHours =
        remember(state.weatherInfo) {
            state.weatherInfo
                ?.weatherDataPerDay
                ?.values
                ?.flatten()
                ?.sortedBy { it.time }
                ?.upcomingHours(count = 24)
                .orEmpty()
        }

    if (upcomingHours.isEmpty()) {
        return
    }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "Next 24 hours",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                items(items = upcomingHours, key = { weather -> weather.time }) { weatherData ->
                    HourlyWeatherDisplay(
                        weatherData = weatherData,
                        modifier = Modifier.height(100.dp),
                    )
                }
            },
        )
    }
}
