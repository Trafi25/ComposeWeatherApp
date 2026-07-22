package com.plcoding.weatherapp.presentation.weather.daily

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.weatherapp.domain.weather.DailyWeatherData

@Composable
fun SevenDayForecast(
    dailyWeather: List<DailyWeatherData>,
    modifier: Modifier = Modifier,
) {
    if (dailyWeather.isEmpty()) return
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text(
            text = "7-days forecast",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        dailyWeather.forEachIndexed { index, weather ->
            DailyWeatherItem(weather = weather)
            if (index < dailyWeather.lastIndex) {
                HorizontalDivider(color = Color.White)
            }
        }
    }
}
