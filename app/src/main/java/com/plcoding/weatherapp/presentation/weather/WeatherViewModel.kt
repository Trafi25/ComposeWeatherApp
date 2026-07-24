package com.plcoding.weatherapp.presentation.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.location.LocationNameResolver
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.location.displayName
import com.plcoding.weatherapp.domain.repository.CityRepository
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.util.WeatherError
import com.plcoding.weatherapp.domain.util.toMessage
import com.plcoding.weatherapp.presentation.weather.states.CitySearchState
import com.plcoding.weatherapp.presentation.weather.states.WeatherScreenMode
import com.plcoding.weatherapp.presentation.weather.states.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
        private val cityRepository: CityRepository,
        private val repository: WeatherRepository,
        private val locationTracker: LocationTracker,
        private val locationNameResolver: LocationNameResolver,
    ) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherState())
    val uiState: StateFlow<WeatherState> = _uiState.asStateFlow()
    private var loadWeatherJob: Job? = null
    private var citySearchJob: Job? = null

    private fun loadWeatherInfo() {
        loadWeatherJob?.cancel()

        loadWeatherJob =
            viewModelScope.launch {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = true,
                        errorMessage = null,
                    )
                }

                val location = locationTracker.getCurrentLocation()

                Log.d(
                    "WeatherLocation",
                    "Latitude: ${location?.latitude}, Longitude: ${location?.longitude}",
                )

                if (location == null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = WeatherError.LocationUnavailable.toMessage(),
                        )
                    }

                    return@launch
                }

                val locationName =
                    locationNameResolver.getLocationName(
                        latitude = location.latitude,
                        longitude = location.longitude,
                    )

                _uiState.update { currentState ->
                    currentState.copy(
                        locationName = locationName,
                    )
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
            val result =
                repository.getWeatherData(
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

    private fun openManageCities() {
        _uiState.update { state -> state.copy(screenMode = WeatherScreenMode.ManageCities) }
    }

    private fun openCitySearch() {
        _uiState.update { state ->
            state.copy(
                screenMode = WeatherScreenMode.SearchCity,
                citySearch = CitySearchState()
            )
        }
    }

    private fun onBackClicked() {
        citySearchJob?.cancel()
        _uiState.update { state ->
            when (state.screenMode) {
                WeatherScreenMode.SearchCity ->
                    state.copy(
                        screenMode = WeatherScreenMode.ManageCities,
                        citySearch = CitySearchState(),
                    )

                WeatherScreenMode.ManageCities ->
                    state.copy(
                        screenMode = WeatherScreenMode.Weather,
                    )

                WeatherScreenMode.Weather ->
                    state
            }
        }
    }

    private fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                citySearch = currentState.citySearch.copy(query = query, errorMessage = null),
            )
        }
        citySearchJob?.cancel()
        val trimmedQuery = query.trim()

        if (trimmedQuery.length < 2) {
            _uiState.update { state ->
                state.copy(
                    citySearch =
                        state.citySearch.copy(
                            results = emptyList(),
                            isLoading = false,
                        ),
                )
            }
            return
        }
        citySearchJob =
            viewModelScope.launch {
                delay(400)
                searchCities(trimmedQuery)
            }
    }

    private suspend fun searchCities(query: String) {
        _uiState.update { state ->
            state.copy(
                citySearch =
                    state.citySearch.copy(
                        isLoading = true,
                        errorMessage = null,
                    ),
            )
        }
        when (val result = cityRepository.searchCities(query)) {
            is Result.Success -> {
                _uiState.update { state ->
                    state.copy(
                        citySearch =
                            state.citySearch.copy(
                                results = result.data,
                                isLoading = false,
                            ),
                    )
                }
            }

            is Result.Error -> {
                _uiState.update { state ->
                    state.copy(
                        citySearch =
                            state.citySearch.copy(
                                results = emptyList(),
                                isLoading = false,
                                errorMessage = result.error.toMessage(),
                            ),
                    )
                }
            }
        }
    }

    private fun closeCitySearch() {
        citySearchJob?.cancel()
        _uiState.update { state ->
            state.copy(
                citySearch = CitySearchState(),
            )
        }
    }

    private fun onCitySelected(city: City) {
        citySearchJob?.cancel()
        loadWeatherJob?.cancel()
        val displayName = city.displayName()
        _uiState.update { state ->
            state.copy(
                citySearch = CitySearchState(),
                isLoading = true,
                errorMessage = null
            )
        }
        loadWeatherJob =
            viewModelScope.launch {
                when (val result = repository.getWeatherData(
                    lat = city.latitude,
                    long = city.longitude
                )) {
                    is Result.Success -> _uiState.update { state ->
                        state.copy(
                            weatherInfo = result.data,
                            locationName = displayName,
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    is Result.Error -> _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = result.error.toMessage()
                        )
                    }
                }
            }
    }

    fun onAction(action: WeatherAction) {
        when (action) {
            WeatherAction.LoadWeather,
            WeatherAction.Retry,
            WeatherAction.LocationPermissionGranted,
                -> loadWeatherInfo()

            WeatherAction.ManageCitiesClicked ->
                openManageCities()
            WeatherAction.AddCityClicked ->
                openCitySearch()
            WeatherAction.BackClicked ->
                onBackClicked()
            WeatherAction.UseCurrentLocationClicked -> {
                _uiState.update { state ->
                    state.copy(
                        screenMode = WeatherScreenMode.Weather,
                        citySearch = CitySearchState(),
                    )
                }
                loadWeatherInfo()
            }
            is WeatherAction.SearchQueryChanged ->
                updateSearchQuery(action.query)
            is WeatherAction.CitySelected ->
                onCitySelected(action.city)
            WeatherAction.LocationPermissionDenied -> {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage =
                            "Location permission is required to load weather.",
                    )
                }
            }
            WeatherAction.ErrorDismissed -> {
                _uiState.update { state ->
                    state.copy(errorMessage = null)
                }
            }

            WeatherAction.RequestLocationPermission -> Unit
        }
    }
}
