package com.plcoding.weatherapp.presentation.weather.curent

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
import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.domain.weather.CurrentWeatherData
import com.plcoding.weatherapp.presentation.formatter.LocalWeatherValueFormatter
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherSummary(
    data: CurrentWeatherData,
    settings: AppSettings,
    modifier: Modifier = Modifier,
) {
    val formatter = LocalWeatherValueFormatter.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        WeatherDataDisplay(
            displayValue = formatter.formatPressure(data.pressure, settings.pressureUnit),
            icon = ImageVector.vectorResource(id = R.drawable.ic_pressure),
            iconTint = Color.White,
            textStyle = TextStyle(color = Color.White),
        )
        WeatherDataDisplay(
            displayValue = "${data.humidity.roundToInt()}%",
            icon = ImageVector.vectorResource(id = R.drawable.ic_drop),
            iconTint = Color.White,
            textStyle = TextStyle(color = Color.White),
        )
        WeatherDataDisplay(
            displayValue = formatter.formatWindSpeed(data.windSpeed, settings.windSpeedUnit),
            icon = ImageVector.vectorResource(id = R.drawable.ic_wind),
            iconTint = Color.White,
            textStyle = TextStyle(color = Color.White),
        )
    }
}
