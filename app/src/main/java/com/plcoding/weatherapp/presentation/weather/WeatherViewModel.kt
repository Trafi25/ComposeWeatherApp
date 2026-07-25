package com.plcoding.weatherapp.presentation.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.location.LocationNameResolver
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.CityRepository
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.DataError
import com.plcoding.weatherapp.domain.util.Result
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
        private val weatherRepository: WeatherRepository,
        private val locationTracker: LocationTracker,
        private val locationNameResolver: LocationNameResolver,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(WeatherState())
        val uiState: StateFlow<WeatherState> = _uiState.asStateFlow()
        private var loadWeatherJob: Job? = null
        private var citySearchJob: Job? = null
        private var selectedCity: City? = null

        private companion object {
            const val MIN_CITY_QUERY_LENGTH = 2
            const val CITY_SEARCH_DEBOUNCE = 500L
        }

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
                                errorMessage = DataError.LocationUnavailable.toMessage(),
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
                    weatherRepository.getWeatherData(
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
                    citySearchState = state.citySearchState.copy(query = "", results = emptyList()),
                )
            }
        }

        private fun showWeatherScreen() {
            citySearchJob?.cancel()
            _uiState.update { currentState ->
                currentState.copy(
                    screenMode = WeatherScreenMode.Weather,
                )
            }
        }

        private fun updateSearchQuery(query: String) {
            _uiState.update { currentState ->
                currentState.copy(
                    citySearchState =
                        currentState.citySearchState.copy(
                            query = query,
                            errorMessage = null,
                        ),
                )
            }
            citySearchJob?.cancel()
            if (query.length < MIN_CITY_QUERY_LENGTH) {
                _uiState.update { currentState ->
                    currentState.copy(
                        citySearchState =
                            currentState.citySearchState.copy(
                                results = emptyList(),
                                isLoading = false,
                            ),
                    )
                }
                return
            }
            citySearchJob =
                viewModelScope.launch {
                    delay(CITY_SEARCH_DEBOUNCE)
                    searchCities(query)
                }
        }

        private suspend fun searchCities(query: String) {
            _uiState.update { currentState ->
                currentState.copy(
                    citySearchState =
                        currentState.citySearchState.copy(
                            isLoading = true,
                            errorMessage = null,
                        ),
                )
            }
            when (val result = cityRepository.searchCities(query = query)) {
                is Result.Success -> {
                    _uiState.update { currentState ->
                        if (currentState.citySearchState.query != query) {
                            currentState
                        } else {
                            currentState.copy(
                                citySearchState =
                                    currentState.citySearchState.copy(
                                        results = result.data,
                                        isLoading = false,
                                        errorMessage = null,
                                    ),
                            )
                        }
                    }
                }

                is Result.Error -> {
                    _uiState.update { currentState ->
                        if (currentState.citySearchState.query != query) {
                            currentState
                        } else {
                            currentState.copy(
                                citySearchState =
                                    currentState.citySearchState.copy(
                                        results = emptyList(),
                                        isLoading = false,
                                        errorMessage = result.error.toMessage(),
                                    ),
                            )
                        }
                    }
                }
            }
        }

        private fun loadWeatherForCity(city: City) {
            citySearchJob?.cancel()
            selectedCity = city
            viewModelScope.launch {
                _uiState.update { currentState ->
                    currentState.copy(
                        locationName = city.name,
                        screenMode = WeatherScreenMode.Weather,
                        citySearchState = CitySearchState(),
                        isLoading = true,
                        errorMessage = null,
                        savedCities = currentState.savedCities.addCityIfMissing(city),
                    )
                }
                when (
                    val result =
                        weatherRepository.getWeatherData(
                            lat = city.latitude,
                            long = city.longitude,
                        )
                ) {
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                weatherInfo = result.data,
                                isLoading = false,
                                locationName = city.name,
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
        }

        private fun List<City>.addCityIfMissing(city: City): List<City> {
            val cityAlreadyExists =
                any { savedCity ->
                    savedCity.id == city.id
                }
            return if (cityAlreadyExists) this else this + city
        }

        fun onAction(action: WeatherAction) {
            when (action) {
                WeatherAction.SearchCityClicked -> {
                    openCitySearch()
                }
                WeatherAction.ManageCitiesClicked -> {
                    openManageCities()
                }
                WeatherAction.CityScreenBackClicked -> {
                    showWeatherScreen()
                }
                WeatherAction.LoadWeather -> {
                    loadWeatherInfo()
                }
                WeatherAction.Retry -> {
                    selectedCity?.let { city -> loadWeatherForCity(city) }
                        ?: loadWeatherInfo()
                }
                WeatherAction.ErrorDismissed -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = null,
                        )
                    }
                }
                WeatherAction.RequestLocationPermission -> {
                }
                WeatherAction.LocationPermissionGranted -> {
                    loadWeatherInfo()
                }
                WeatherAction.LocationPermissionDenied -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = "Location permission is required.",
                        )
                    }
                }
                is WeatherAction.SearchQueryChanged -> {
                    updateSearchQuery(action.query)
                }
                is WeatherAction.CitySelected -> {
                    loadWeatherForCity(action.city)
                }
            }
        }
    }
