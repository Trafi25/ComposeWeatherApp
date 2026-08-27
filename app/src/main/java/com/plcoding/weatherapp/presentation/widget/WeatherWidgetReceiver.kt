package com.plcoding.weatherapp.presentation.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.plcoding.weatherapp.data.preferences.LastLocationStorage
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.SelectedLocationRepository
import com.plcoding.weatherapp.domain.repository.SettingsRepository
import com.plcoding.weatherapp.domain.usecase.GetWeatherUseCase
import com.plcoding.weatherapp.domain.usecase.SavedCityUseCases
import com.plcoding.weatherapp.presentation.formatter.WeatherValueFormatter
import com.plcoding.weatherapp.presentation.widget.worker.WeatherWidgetScheduler
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget get() = WeatherWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WeatherWidgetScheduler.schedule(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WeatherWidgetScheduler.cancel(context)
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WeatherWidgetEntryPoint {
    fun getWeatherUseCase(): GetWeatherUseCase

    fun getLocationTracker(): LocationTracker

    fun getSelectedLocationRepository(): SelectedLocationRepository

    fun getSavedCityUseCases(): SavedCityUseCases

    fun getSettingsRepository(): SettingsRepository

    fun getWeatherValueFormatter(): WeatherValueFormatter

    fun getLastLocationStorage(): LastLocationStorage
}
