package com.plcoding.weatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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
        fun loadWeatherInfo() {
            viewModelScope.launch {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = true,
                        errorMessage = null,
                    )
                }
                locationTracker.getCurrentLocation()?.let { location ->
                    when (val result = repository.getWeatherData(location.latitude, location.longitude)) {
                        is Resource.Success -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    weatherInfo = result.data,
                                    isLoading = false,
                                    errorMessage = null,
                                )
                            }
                        }
                        is Resource.Error -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    weatherInfo = null,
                                    isLoading = false,
                                    errorMessage = result.message,
                                )
                            }
                        }
                    }
                } ?: kotlin.run {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "Couldn't retrieve location. Make sure to grant permission and enable GPS.",
                        )
                    }
                }
            }
        }
    }
