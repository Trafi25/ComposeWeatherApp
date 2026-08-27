package com.plcoding.weatherapp.presentation.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.presentation.widget.components.NoLocationContent
import com.plcoding.weatherapp.presentation.widget.components.WeatherWidgetContent
import com.plcoding.weatherapp.presentation.widget.components.WeatherWidgetErrorContent
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first

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
        val lastLocationStorage = entryPoint.getLastLocationStorage()

        val settings = settingsRepository.observeSettings().first()
        val selectedCityId = selectedLocationRepository.observeSelectedCityId().first()

        // 1. Визначаємо місто згідно з вибором у додатку
        val selectedCity = selectedCityId?.let { cityUseCases.getCity(it) }

        // 2. Визначаємо координати та назву (Пріоритет: Вибране місто -> Свіжий GPS -> Кеш додатка)
        val (latitude, longitude, locationName) =
            if (selectedCity != null) {
                Triple(selectedCity.latitude, selectedCity.longitude, selectedCity.name)
            } else {
                val gpsLocation = locationTracker.getCurrentLocation()
                val cachedData = lastLocationStorage.observeLocation().first()

                if (gpsLocation != null) {
                    Triple(gpsLocation.latitude, gpsLocation.longitude, "Current location")
                } else if (cachedData != null) {
                    Triple(cachedData.first, cachedData.second, cachedData.third ?: "Current location")
                } else {
                    Triple(null, null, null)
                }
            }

        if (latitude == null || longitude == null) {
            provideContent { NoLocationContent() }
            return
        }

        val result = getWeatherUseCase(latitude, longitude)

        provideContent {
            when (result) {
                is Result.Success -> {
                    WeatherWidgetContent(
                        locationName = locationName ?: "Current location",
                        weatherInfo = result.data,
                        settings = settings,
                        formatter = formatter,
                    )
                }
                is Result.Error -> WeatherWidgetErrorContent()
            }
        }
    }
}
