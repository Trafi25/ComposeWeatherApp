package com.plcoding.weatherapp.notification

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.plcoding.weatherapp.data.preferences.LastLocationStorage
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.SelectedLocationRepository
import com.plcoding.weatherapp.domain.repository.SettingsRepository
import com.plcoding.weatherapp.domain.usecase.GetWeatherUseCase
import com.plcoding.weatherapp.domain.usecase.SavedCityUseCases
import com.plcoding.weatherapp.notification.WeatherNotificationManager.showNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import androidx.work.ListenableWorker.Result as WorkerResult

@HiltWorker
class WeatherNotificationWorker
    @AssistedInject
    constructor(
        @Assisted private val context: Context,
        @Assisted workerParameters: WorkerParameters,
        private val getWeatherUseCase: GetWeatherUseCase,
        private val selectedLocationRepository: SelectedLocationRepository,
        private val cityUseCases: SavedCityUseCases,
        private val locationTracker: LocationTracker,
        private val settingsRepository: SettingsRepository,
        private val lastLocationStorage: LastLocationStorage,
    ) : CoroutineWorker(context, workerParameters) {
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        override suspend fun doWork(): WorkerResult {
            val settings = settingsRepository.observeSettings().first()
            if (!settings.weatherNotificationEnabled) {
                return Result.success()
            }

            val selectedCityId = selectedLocationRepository.observeSelectedCityId().first()

            val coordinates: Pair<Double, Double>
            val locationName: String

            if (selectedCityId != null) {
                val city = cityUseCases.getCity(selectedCityId) ?: return Result.retry()

                coordinates = city.latitude to city.longitude

                locationName = city.name
            } else {
                val location = locationTracker.getCurrentLocation()

                if (location != null) {
                    coordinates = location.latitude to location.longitude
                    lastLocationStorage.save(location.latitude, location.longitude)
                } else {
                    coordinates = lastLocationStorage.observeLocation().first() ?: return Result.retry()
                }

                locationName = "Current location"
            }

            return when (val weatherResult = getWeatherUseCase(coordinates.first, coordinates.second)) {
                is com.plcoding.weatherapp.domain.util.Result.Success -> {
                    showNotification(
                        locationName = locationName,
                        weatherInfo = weatherResult.data,
                        context = context,
                    )
                    Result.success()
                }

                is com.plcoding.weatherapp.domain.util.Result.Error -> {
                    Result.retry()
                }
            }
        }
    }
