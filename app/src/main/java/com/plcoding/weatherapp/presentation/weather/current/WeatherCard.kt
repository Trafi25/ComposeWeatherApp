package com.plcoding.weatherapp.presentation.weather.current

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.presentation.formatter.LocalWeatherValueFormatter
import com.plcoding.weatherapp.presentation.ui.theme.sizes
import com.plcoding.weatherapp.presentation.ui.theme.spacing
import com.plcoding.weatherapp.presentation.weather.state.WeatherState

@Composable
fun WeatherCard(
    state: WeatherState,
    locationName: String?,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    val formatter = LocalWeatherValueFormatter.current
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    state.weatherInfo?.currentWeatherData?.let { data ->
        Card(
            backgroundColor = backgroundColor,
            shape = MaterialTheme.shapes.medium,
            modifier = modifier.padding(MaterialTheme.spacing.medium),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CurrentDayTime(
                    modifier = Modifier.align(Alignment.End),
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Text(
                    text = locationName ?: "Current location",
                    modifier = Modifier.align(Alignment.End),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Image(
                    painter = painterResource(id = data.weatherType.getIconRes(data.isDay)),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .fillMaxWidth(0.35f)
                            .aspectRatio(1f),
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Text(
                    text =
                        formatter.formatTemperature(
                            data.temperatureCelsius,
                            state.appSettings.temperatureUnit,
                        ),
                    fontSize = 50.sp,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Text(
                    text = data.weatherType.weatherDesc,
                    fontSize = 20.sp,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                CurrentWeatherSummary(
                    data = data,
                    settings = state.appSettings,
                )
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut(),
                ) {
                    AdditionalWeatherDetails(
                        data = data,
                        settings = state.appSettings,
                    )
                }
                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
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
                        modifier = Modifier.size(MaterialTheme.sizes.iconSmall),
                    )
                }
            }
        }
    }
}
