package com.plcoding.weatherapp.presentation.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherWidgetUpdater @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun update() {
        WeatherWidget().updateAll(context)
    }
}
