package com.plcoding.weatherapp.presentation.widget.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WeatherWidgetScheduler {
    private const val WORK_NAME = "weather_widget_periodic_update"

    fun schedule(context: Context) {
        val request =
            PeriodicWorkRequestBuilder<WeatherWidgetWorker>(
                1,
                TimeUnit.HOURS,
            ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}
