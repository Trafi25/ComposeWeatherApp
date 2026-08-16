package com.plcoding.weatherapp.presentation.weather.hourly

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.weatherapp.domain.weather.toWeatherData
import com.plcoding.weatherapp.domain.weather.upcomingHours
import com.plcoding.weatherapp.presentation.formatter.LocalWeatherValueFormatter
import com.plcoding.weatherapp.presentation.weather.state.WeatherState
import java.time.LocalDateTime

@Composable
fun WeatherForecast(
    state: WeatherState,
    modifier: Modifier = Modifier,
) {
    val upcomingHours =
        remember(state.weatherInfo, state.lastUpdated) {
            val currentData = state.weatherInfo?.currentWeatherData
            val referenceTime = currentData?.time ?: LocalDateTime.now()

            val hourly =
                state.weatherInfo
                    ?.weatherDataPerDay
                    ?.values
                    ?.flatten()
                    ?.sortedBy { it.time }
                    ?.upcomingHours(now = referenceTime, count = 24)
                    .orEmpty()

            if (currentData != null && hourly.isNotEmpty()) {
                listOf(currentData.toWeatherData()) + hourly.drop(1)
            } else {
                hourly
            }
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
        val valueFormatter = LocalWeatherValueFormatter.current

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                itemsIndexed(items = upcomingHours) { index, weatherData ->
                    val displayTime =
                        if (index == 0) {
                            "Now"
                        } else {
                            valueFormatter.formatTime(weatherData.time, state.appSettings.timeFormat)
                        }

                    HourlyWeatherDisplay(
                        weatherData = weatherData,
                        temperatureUnit = state.appSettings.temperatureUnit,
                        displayTime = displayTime,
                        isCurrent = index == 0,
                        modifier = Modifier.height(100.dp),
                    )
                }
            },
        )
    }
}
