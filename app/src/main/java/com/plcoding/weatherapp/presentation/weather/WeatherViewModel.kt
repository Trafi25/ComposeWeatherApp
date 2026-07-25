package com.plcoding.weatherapp.presentation.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.location.LocationNameResolver
import com.plcoding.weatherapp.domain.location.LocationTracker
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
                    citySearchState = state.citySearchState.copy(query = "", results = emptyList()),
                )
            }
        }

        private fun showWeatherScreen() {
            _uiState.update { currentState ->
                currentState.copy(
                    screenMode = WeatherScreenMode.Weather,
                )
            }
        }

//    private fun onBackClicked() {
//        citySearchJob?.cancel()
//        _uiState.update { state ->
//            when (state.screenMode) {
//                WeatherScreenMode.SearchCity ->
//                    state.copy(
//                        screenMode = WeatherScreenMode.ManageCities,
//                        citySearch = CitySearchState(),
//                    )
//
//                WeatherScreenMode.ManageCities ->
//                    state.copy(
//                        screenMode = WeatherScreenMode.Weather,
//                    )
//
//                WeatherScreenMode.Weather ->
//                    state
//            }
//        }
//    }

        private fun updateSearchQuery(query: String) {
            val filteredCities: List<City> =
                (
                    if (query.isBlank()) {
                        emptyList()
                    } else {
                        availableCities.filter { city ->
                            city.name.contains(
                                other = query,
                                ignoreCase = true,
                            )
                        }
                    }
                ) as List<City>
            _uiState.update { currentState ->
                currentState.copy(
                    citySearchState =
                        currentState.citySearchState.copy(
                            query = query,
                            results = filteredCities,
                        ),
                )
            }
        }

        private fun selectCity(city: City) {
            _uiState.update { currentState ->
                currentState.copy(
                    locationName = city.name,
                    screenMode = WeatherScreenMode.Weather,
                    citySearchState = CitySearchState(),
                )
            }
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
                    loadWeatherInfo()
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
                    selectCity(action.city)
                }
            }
        }
    }

private val availableCities =
    listOf(
        City(
            id = 2867714,
            name = "Munich",
            country = "Germany",
            adminArea = "Bavaria",
            latitude = 48.1374,
            longitude = 11.5755,
            timezone = "Europe/Berlin",
        ),
        City(
            id = 2861650,
            name = "Nuremberg",
            country = "Germany",
            adminArea = "Bavaria",
            latitude = 49.4521,
            longitude = 11.0767,
            timezone = "Europe/Berlin",
        ),
        City(
            id = 2950159,
            name = "Berlin",
            country = "Germany",
            adminArea = "Berlin",
            latitude = 52.5200,
            longitude = 13.4050,
            timezone = "Europe/Berlin",
        ),
        City(
            id = 2911298,
            name = "Hamburg",
            country = "Germany",
            adminArea = "Hamburg",
            latitude = 53.5511,
            longitude = 9.9937,
            timezone = "Europe/Berlin",
        ),
        City(
            id = 3094802,
            name = "Krakow",
            country = "Poland",
            adminArea = "Lesser Poland",
            latitude = 50.0647,
            longitude = 19.9450,
            timezone = "Europe/Warsaw",
        ),
        City(
            id = 756135,
            name = "Warsaw",
            country = "Poland",
            adminArea = "Masovian Voivodeship",
            latitude = 52.2297,
            longitude = 21.0122,
            timezone = "Europe/Warsaw",
        ),
    )
