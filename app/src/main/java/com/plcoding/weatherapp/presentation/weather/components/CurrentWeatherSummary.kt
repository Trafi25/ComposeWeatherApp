package com.plcoding.weatherapp.presentation.weather.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.domain.weather.CurrentWeatherData
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherSummary(data: CurrentWeatherData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        WeatherDataDisplay(
            value = data.pressure.roundToInt(),
            unit = "hpa",
            icon = ImageVector.vectorResource(id = R.drawable.ic_pressure),
            iconTint = Color.White,
            textStyle = TextStyle(color = Color.White),
        )
        WeatherDataDisplay(
            value = data.humidity.roundToInt(),
            unit = "%",
            icon = ImageVector.vectorResource(id = R.drawable.ic_drop),
            iconTint = Color.White,
            textStyle = TextStyle(color = Color.White),
        )
        WeatherDataDisplay(
            value = data.windSpeed.roundToInt(),
            unit = "km/h",
            icon = ImageVector.vectorResource(id = R.drawable.ic_wind),
            iconTint = Color.White,
            textStyle = TextStyle(color = Color.White),
        )
    }
}
