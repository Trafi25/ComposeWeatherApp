package com.plcoding.weatherapp.presentation.weather.curent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.domain.weather.CurrentWeatherData
import kotlin.math.roundToInt

@Composable
fun AdditionalWeatherDetails(
    data: CurrentWeatherData,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HorizontalDivider(
            color = Color.White,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            WeatherDetailItem(
                iconResource = R.drawable.feelsliketemoerature,
                label = "Feels like",
                value = "${data.apparentTemperatureCelsius.roundToInt()}°C",
                modifier = Modifier.weight(1f),
            )
            WeatherDetailItem(
                iconResource = R.drawable.presopatation,
                label = "Precipitation",
                value = "${data.precipitationMm} mm",
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            WeatherDetailItem(
                iconResource = R.drawable.cloudcover,
                label = "Cloud cover",
                value = "${data.cloudCoverPercent}%",
                modifier = Modifier.weight(1f),
            )
            WeatherDetailItem(
                iconResource = R.drawable.surfacepressure,
                label = "Surface pressure",
                value = "${data.pressure.roundToInt()} hPa",
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            WeatherDetailItem(
                iconResource = R.drawable.winddirrection,
                label = "Wind direction",
                value = "${data.windDirectionLabel} ${data.windDirectionDegrees}°",
                modifier = Modifier.weight(1f),
            )
            WeatherDetailItem(
                iconResource = R.drawable.windguts,
                label = "Wind gusts",
                value = "${data.windGustsKmh.roundToInt()} km/h",
                modifier = Modifier.weight(1f),
            )
        }
    }
}
