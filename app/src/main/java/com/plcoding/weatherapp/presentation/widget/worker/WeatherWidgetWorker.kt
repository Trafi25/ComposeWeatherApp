package com.plcoding.weatherapp.presentation.widget.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.plcoding.weatherapp.presentation.widget.WeatherWidget
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherWidgetWorker
    @AssistedInject
    constructor(
        @Assisted private val context: Context,
        @Assisted workerParams: WorkerParameters,
    ) : CoroutineWorker(context, workerParams) {
        override suspend fun doWork(): Result {
            WeatherWidget().updateAll(context)
            return Result.success()
        }
    }
