package com.plcoding.weatherapp.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.location.LocationNameResolver
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.CityRepository
import com.plcoding.weatherapp.domain.repository.SavedCityRepository
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
    @Inject
    constructor(
        private val weatherRepository: WeatherRepository,
        private val cityRepository: CityRepository,
        private val savedCityRepository: SavedCityRepository,
        private val locationTracker: LocationTracker,
        private val locationNameResolver: LocationNameResolver,
    ) : ViewModel() {
        init {
            observeSavedCities()
        }

        private val _uiState = MutableStateFlow(WeatherState())
        val uiState: StateFlow<WeatherState> = _uiState.asStateFlow()
        private var loadWeatherJob: Job? = null
        private var citySearchJob: Job? = null

        private companion object {
            const val MIN_CITY_QUERY_LENGTH = 2
            const val CITY_SEARCH_DEBOUNCE = 500L
        }

        private fun observeSavedCities() {
            viewModelScope.launch {
                savedCityRepository.observeSavedCities().collect { cities ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            savedCities = cities,
                        )
                    }
                }
            }
        }

        private fun loadWeatherInfo() {
            viewModelScope.launch {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = true,
                        errorMessage = null,
                        selectedCityId = null,
                    )
                }

                locationTracker.getCurrentLocation()?.let { location ->
                    when (
                        val result =
                            weatherRepository.getWeatherData(
                                lat = location.latitude,
                                long = location.longitude,
                            )
                    ) {
                        is Result.Success -> {
                            val locationName =
                                locationNameResolver.getLocationName(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                )
                            _uiState.update { currentState ->
                                currentState.copy(
                                    weatherInfo = result.data,
                                    isLoading = false,
                                    locationName = locationName,
                                    selectedCityId = null,
                                )
                            }
                        }
                        is Result.Error -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    errorMessage = DataError.LocationUnavailable.toMessage(),
                                )
                            }
                        }
                    }
                } ?: run {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "Couldn't retrieve current location.",
                        )
                    }
                }
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
            viewModelScope.launch {
                savedCityRepository.saveCity(city)
                _uiState.update { currentState ->
                    currentState.copy(
                        locationName = city.name,
                        screenMode = WeatherScreenMode.Weather,
                        citySearchState = CitySearchState(),
                        isLoading = true,
                        errorMessage = null,
                        selectedCityId = city.id,
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

        private fun deleteSavedCity(cityId: Int) {
            val isSelectedCity =
                _uiState.value.selectedCityId == cityId

            viewModelScope.launch {
                savedCityRepository.deleteCity(cityId)

                if (isSelectedCity) {
                    selectCurrentLocation()
                }
            }
        }

        private fun retryWeatherLoading() {
            val selectedCityId = _uiState.value.selectedCityId
            if (selectedCityId == null) {
                loadWeatherInfo()
                return
            }
            viewModelScope.launch {
                val selectedCity = savedCityRepository.getCity(selectedCityId)

                if (selectedCity != null) {
                    loadWeatherForCity(selectedCity)
                } else {
                    selectCurrentLocation()
                }
            }
        }

        private fun selectCurrentLocation() {
            citySearchJob?.cancel()
            _uiState.update { currentState ->
                currentState.copy(
                    selectedCityId = null,
                    screenMode = WeatherScreenMode.Weather,
                    errorMessage = null,
                )
            }
            loadWeatherInfo()
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
                    retryWeatherLoading()
                }
                WeatherAction.ErrorDismissed -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = null,
                        )
                    }
                }
                WeatherAction.CurrentLocationSelected -> {
                    selectCurrentLocation()
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
                is WeatherAction.SavedCityDeleted -> {
                    deleteSavedCity(action.cityId)
                }
            }
        }
    }
