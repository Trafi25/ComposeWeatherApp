package com.plcoding.weatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.util.WeatherError
import com.plcoding.weatherapp.domain.util.toMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
    @Inject
    constructor(
        private val repository: WeatherRepository,
        private val locationTracker: LocationTracker,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherState())
    val uiState: StateFlow<WeatherState> = _uiState.asStateFlow()
    private var loadWeatherJob: Job? = null
    fun loadWeatherInfo() {
        loadWeatherJob?.cancel()

        loadWeatherJob = viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }

            val location = locationTracker.getCurrentLocation()

            if (location == null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = WeatherError.LocationUnavailable.toMessage(),
                    )
                }

                return@launch
            }

            loadWeatherForLocation(
                latitude = location.latitude,
                longitude = location.longitude,
            )
        }
    }


    private suspend fun loadWeatherForLocation(
        latitude: Double,
        longitude: Double,
    ) {
        when (
            val result = repository.getWeatherData(
                lat = latitude,
                long = longitude,
            )
        ) {
            is Result.Success -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        weatherInfo = result.data,
                        isLoading = false,
                        errorMessage = null,
                    )
                }
            }

            is Result.Error -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = result.error.toMessage(),
                    )
                }
            }
        }
    }


    fun onAction(action: WeatherAction) {
        when (action) {
            WeatherAction.LoadWeather,
            WeatherAction.Retry,
            WeatherAction.LocationPermissionGranted -> {
                loadWeatherInfo()
            }

            WeatherAction.ErrorDismissed -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage = null,
                    )
                }
            }
        }
    }


}
