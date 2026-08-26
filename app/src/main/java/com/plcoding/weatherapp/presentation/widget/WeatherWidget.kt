package com.plcoding.weatherapp.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.weather.WeatherData
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import com.plcoding.weatherapp.domain.weather.upcomingHours
import com.plcoding.weatherapp.presentation.MainActivity
import com.plcoding.weatherapp.presentation.formatter.WeatherValueFormatter
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first

@SuppressLint("RestrictedApi")
private val WhiteColorProvider = ColorProvider(Color.White)

class WeatherWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        val entryPoint =
            EntryPointAccessors.fromApplication(
                context,
                WeatherWidgetEntryPoint::class.java,
            )
        val getWeatherUseCase = entryPoint.getWeatherUseCase()
        val locationTracker = entryPoint.getLocationTracker()
        val selectedLocationRepository = entryPoint.getSelectedLocationRepository()
        val cityUseCases = entryPoint.getSavedCityUseCases()
        val settingsRepository = entryPoint.getSettingsRepository()
        val formatter = entryPoint.getWeatherValueFormatter()

        val settings = settingsRepository.observeSettings().first()
        val selectedCityId = selectedLocationRepository.observeSelectedCityId().first()

        var coordinates: Pair<Double, Double>? = null
        var locationName = "Current location"

        if (selectedCityId != null) {
            val city = cityUseCases.getCity(selectedCityId)
            if (city != null) {
                coordinates = city.latitude to city.longitude
                locationName = city.name
            }
        } else {
            val location = locationTracker.getCurrentLocation()
            if (location != null) {
                coordinates = location.latitude to location.longitude
            }
        }

        if (coordinates == null) {
            provideContent {
                Box(
                    modifier =
                        GlanceModifier
                            .fillMaxSize()
                            .background(ImageProvider(R.drawable.bg_widget))
                            .clickable(actionStartActivity<MainActivity>()),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No location data",
                            style =
                                TextStyle(
                                    color = WhiteColorProvider,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                        )
                        Spacer(modifier = GlanceModifier.height(8.dp))
                        Text(
                            text = "Tap to open app and set location",
                            style =
                                TextStyle(
                                    color = WhiteColorProvider,
                                    fontSize = 12.sp,
                                ),
                        )
                    }
                }
            }
            return
        }

        val (latitude, longitude) = coordinates
        val result = getWeatherUseCase(latitude, longitude)

        provideContent {
            when (result) {
                is Result.Success -> {
                    WeatherWidgetContent(
                        locationName = locationName,
                        weatherInfo = result.data,
                        settings = settings,
                        formatter = formatter,
                    )
                }

                is Result.Error -> {
                    Box(
                        modifier =
                            GlanceModifier
                                .fillMaxSize()
                                .background(ImageProvider(R.drawable.bg_widget))
                                .clickable(actionStartActivity<MainActivity>()),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Error loading weather. Tap to retry.",
                            style = TextStyle(color = WhiteColorProvider),
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun WeatherWidgetContent(
    locationName: String,
    weatherInfo: WeatherInfo,
    settings: AppSettings,
    formatter: WeatherValueFormatter,
) {
    val allHours = weatherInfo.weatherDataPerDay.values.flatten()
    val upcomingHours = allHours.upcomingHours()

    Box(
        modifier =
            GlanceModifier
                .fillMaxSize()
                .background(ImageProvider(R.drawable.bg_widget)),
    ) {
        LazyColumn(
            modifier =
                GlanceModifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            item {
                weatherInfo.currentWeatherData?.let { current ->
                    Column(
                        modifier = GlanceModifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = locationName,
                            style =
                                TextStyle(
                                    color = WhiteColorProvider,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                            maxLines = 1,
                        )
                        Spacer(modifier = GlanceModifier.height(8.dp))

                        Image(
                            provider =
                                ImageProvider(
                                    current.weatherType.getIconRes(current.isDay),
                                ),
                            contentDescription = null,
                            modifier = GlanceModifier.size(40.dp),
                        )

                        Spacer(modifier = GlanceModifier.height(6.dp))

                        Text(
                            text =
                                formatter.formatTemperature(
                                    current.temperatureCelsius,
                                    settings.temperatureUnit,
                                ),
                            style =
                                TextStyle(
                                    color = WhiteColorProvider,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                        )

                        Text(
                            text = current.weatherType.weatherDesc,
                            style =
                                TextStyle(
                                    color = WhiteColorProvider,
                                    fontSize = 11.sp,
                                ),
                            maxLines = 1,
                        )

                        Spacer(modifier = GlanceModifier.height(12.dp))

                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            DetailColumn(
                                label = "Wind",
                                value =
                                    formatter.formatWindSpeed(
                                        current.windSpeed,
                                        settings.windSpeedUnit,
                                    ),
                            )
                            Spacer(modifier = GlanceModifier.width(8.dp))
                            DetailColumn(
                                label = "Humidity",
                                value = "${current.humidity.toInt()}%",
                            )
                            Spacer(modifier = GlanceModifier.width(8.dp))
                            DetailColumn(
                                label = "Feels like",
                                value =
                                    formatter.formatTemperature(
                                        current.apparentTemperatureCelsius,
                                        settings.temperatureUnit,
                                    ),
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = GlanceModifier.height(12.dp))
                Text(
                    text = "Next 24 Hours",
                    style =
                        TextStyle(
                            color = WhiteColorProvider,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    modifier = GlanceModifier.padding(bottom = 2.dp),
                )
            }

            items(upcomingHours) { hour ->
                HourlyItem(hour, settings, formatter)
            }
        }
    }
}

@Composable
private fun DetailColumn(
    label: String,
    value: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style =
                TextStyle(
                    color = WhiteColorProvider,
                    fontSize = 8.sp,
                ),
        )
        Text(
            text = value,
            style =
                TextStyle(
                    color = WhiteColorProvider,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                ),
        )
    }
}

@Composable
private fun HourlyItem(
    data: WeatherData,
    settings: AppSettings,
    formatter: WeatherValueFormatter,
) {
    Row(
        modifier =
            GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = formatter.formatTime(data.time, settings.timeFormat),
            style =
                TextStyle(
                    color = WhiteColorProvider,
                    fontSize = 9.sp,
                ),
            modifier = GlanceModifier.width(40.dp),
        )
        Image(
            provider = ImageProvider(data.weatherType.getIconRes(data.isDay)),
            contentDescription = null,
            modifier = GlanceModifier.size(14.dp),
        )
        Spacer(modifier = GlanceModifier.width(6.dp))
        Text(
            text =
                formatter.formatTemperature(
                    data.temperatureCelsius,
                    settings.temperatureUnit,
                ),
            style =
                TextStyle(
                    color = WhiteColorProvider,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                ),
            modifier = GlanceModifier.width(40.dp),
        )
        Text(
            text = data.weatherType.weatherDesc,
            style =
                TextStyle(
                    color = WhiteColorProvider,
                    fontSize = 8.sp,
                ),
            maxLines = 1,
        )
    }
}
