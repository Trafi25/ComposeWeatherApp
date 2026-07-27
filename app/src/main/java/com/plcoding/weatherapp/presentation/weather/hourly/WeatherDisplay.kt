package com.plcoding.weatherapp.presentation.weather.hourly

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.domain.settings.TemperatureUnit
import com.plcoding.weatherapp.domain.weather.WeatherData
import com.plcoding.weatherapp.presentation.formatter.LocalWeatherValueFormatter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun HourlyWeatherDisplay(
    weatherData: WeatherData,
    temperatureUnit: TemperatureUnit,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
) {
    val formatter = LocalWeatherValueFormatter.current
    val isNow =
        remember(weatherData) {
            val now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS)
            weatherData.time.truncatedTo(ChronoUnit.HOURS).isEqual(now)
        }

    val formatedTime =
        remember(weatherData) {
            if (isNow) {
                "Now"
            } else {
                weatherData.time.format(
                    DateTimeFormatter.ofPattern("HH:mm"),
                )
            }
        }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = formatedTime,
            color = if (isNow) Color.White else Color.LightGray,
            fontWeight = if (isNow) FontWeight.Bold else FontWeight.Normal,
        )
        Image(
            painter = painterResource(id = weatherData.weatherType.getIconRes(weatherData.isDay)),
            contentDescription = null,
            modifier = Modifier.size(35.dp),
        )
        Text(
            text =
                formatter.formatTemperature(
                    weatherData.temperatureCelsius,
                    temperatureUnit,
                ),
            color = textColor,
            fontWeight = FontWeight.Bold,
        )
    }
}
