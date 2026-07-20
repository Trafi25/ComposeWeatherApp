package com.plcoding.weatherapp.presentation.weather.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.presentation.weather.WeatherState

@Composable
fun WeatherCard(
    state: WeatherState,
    backgroundColor: Color,
    locationName: String?,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    state.weatherInfo?.currentWeatherData?.let { data ->
        Card(
            backgroundColor = backgroundColor,
            shape = RoundedCornerShape(10.dp),
            modifier = modifier.padding(16.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CurrentDayTime(
                    modifier = Modifier.align(Alignment.End),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = locationName ?: "Current location",
                    modifier = Modifier.align(Alignment.End),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = data.weatherType.getIconRes(data.isDay)),
                    contentDescription = null,
                    modifier = Modifier.size(170.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${data.temperatureCelsius}°C",
                    fontSize = 50.sp,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = data.weatherType.weatherDesc,
                    fontSize = 20.sp,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(32.dp))
                CurrentWeatherSummary(data)
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    AdditionalWeatherDetails(
                        data = data,
                    )
                }
                IconButton(
                    onClick = {isExpanded = !isExpanded},
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        painter =
                            if (isExpanded) {
                                painterResource(R.drawable.up_arrow)
                            } else {
                                painterResource(R.drawable.down_arrow)
                            },
                        contentDescription =
                            if (isExpanded) {
                                "Collapse weather details"
                            } else {
                                "Expand weather details"
                            },
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                        )
                }
            }
        }
    }
}
