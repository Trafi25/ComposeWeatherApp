package com.plcoding.weatherapp.presentation.weather.hourly

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.domain.settings.TemperatureUnit
import com.plcoding.weatherapp.domain.weather.WeatherData
import com.plcoding.weatherapp.presentation.formatter.LocalWeatherValueFormatter

@Composable
fun HourlyWeatherDisplay(
    weatherData: WeatherData,
    temperatureUnit: TemperatureUnit,
    displayTime: String,
    modifier: Modifier = Modifier,
    isCurrent: Boolean = false,
    textColor: Color = Color.White,
) {
    val formatter = LocalWeatherValueFormatter.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = displayTime,
            color = if (isCurrent) Color.White else Color.LightGray,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
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
