package com.plcoding.weatherapp.notification

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WeatherNotificationScheduler
    @Inject
    constructor(
        @ApplicationContext
        private val context: Context,
    ) {
        fun schedule() {
            val constraints =
                Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val initialDelay = calculateInitialDelay(7, 0)

            val request =
                PeriodicWorkRequestBuilder<WeatherNotificationWorker>(
                    24,
                    TimeUnit.HOURS,
                ).setConstraints(constraints)
                    .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                    .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WEATHER_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request,
            )
        }

        private fun calculateInitialDelay(
            hour: Int,
            minute: Int,
        ): Long {
            val now = LocalDateTime.now()
            val target =
                LocalDateTime
                    .now()
                    .withHour(hour)
                    .withMinute(minute)
                    .withSecond(0)
                    .withNano(0)

            val finalTarget =
                if (now.isAfter(target)) {
                    target.plusDays(1)
                } else {
                    target
                }

            return Duration.between(now, finalTarget).toMillis()
        }

        fun cancel() {
            WorkManager.getInstance(context).cancelUniqueWork(WEATHER_WORK_NAME)
        }

        private companion object {
            const val WEATHER_WORK_NAME =
                "scheduled_weather_notification"
        }
    }
