package com.plcoding.weatherapp.presentation.weather.curent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.domain.weather.CurrentWeatherData
import com.plcoding.weatherapp.presentation.formatter.LocalWeatherValueFormatter
import com.plcoding.weatherapp.presentation.ui.theme.spacing
import kotlin.math.roundToInt

@Composable
fun AdditionalWeatherDetails(
    data: CurrentWeatherData,
    settings: AppSettings,
    modifier: Modifier = Modifier,
) {
    val formatter = LocalWeatherValueFormatter.current

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        HorizontalDivider(
            color = Color.White.copy(alpha = 0.3f),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            WeatherDetailItem(
                iconResource = R.drawable.ic_feels_like,
                label = "Feels like",
                value = formatter.formatTemperature(data.apparentTemperatureCelsius, settings.temperatureUnit),
                modifier = Modifier.weight(1f),
                iconTint = Color.White,
                labelColor = Color.White.copy(alpha = 0.7f),
                valueColor = Color.White,
            )
            WeatherDetailItem(
                iconResource = R.drawable.ic_precipitation,
                label = "Precipitation",
                value = formatter.formatPrecipitation(data.precipitationMm, settings.precipitationUnit),
                modifier = Modifier.weight(1f),
                iconTint = Color.White,
                labelColor = Color.White.copy(alpha = 0.7f),
                valueColor = Color.White,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            WeatherDetailItem(
                iconResource = R.drawable.ic_cloud_cover,
                label = "Cloud cover",
                value = "${data.cloudCoverPercent.roundToInt()}%",
                modifier = Modifier.weight(1f),
                iconTint = Color.White,
                labelColor = Color.White.copy(alpha = 0.7f),
                valueColor = Color.White,
            )
            WeatherDetailItem(
                iconResource = R.drawable.ic_surface_pressure,
                label = "Surface pressure",
                value = formatter.formatPressure(data.pressure, settings.pressureUnit),
                modifier = Modifier.weight(1f),
                iconTint = Color.White,
                labelColor = Color.White.copy(alpha = 0.7f),
                valueColor = Color.White,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            WeatherDetailItem(
                iconResource = R.drawable.ic_wind_direction,
                label = "Wind direction",
                value = "${data.windDirectionLabel} ${data.windDirectionDegrees}°",
                modifier = Modifier.weight(1f),
                iconTint = Color.White,
                labelColor = Color.White.copy(alpha = 0.7f),
                valueColor = Color.White,
            )
            WeatherDetailItem(
                iconResource = R.drawable.ic_wind_gusts,
                label = "Wind gusts",
                value = formatter.formatWindSpeed(data.windGustsKmh, settings.windSpeedUnit),
                modifier = Modifier.weight(1f),
                iconTint = Color.White,
                labelColor = Color.White.copy(alpha = 0.7f),
                valueColor = Color.White,
            )
        }
    }
}
