package com.plcoding.weatherapp.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.data.preferences.LastLocationStorage
import com.plcoding.weatherapp.domain.location.LocationNameResolver
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.SelectedLocationRepository
import com.plcoding.weatherapp.domain.usecase.GenerateWeatherSummaryUseCase
import com.plcoding.weatherapp.domain.usecase.GetWeatherUseCase
import com.plcoding.weatherapp.domain.usecase.SavedCityUseCases
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.util.toMessage
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import com.plcoding.weatherapp.presentation.weather.state.WeatherEffect
import com.plcoding.weatherapp.presentation.weather.state.WeatherScreenMode
import com.plcoding.weatherapp.presentation.weather.state.WeatherState
import com.plcoding.weatherapp.presentation.widget.WeatherWidgetUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class WeatherViewModel
    @Inject
    constructor(
        private val getWeatherUseCase: GetWeatherUseCase,
        private val selectedLocationRepository: SelectedLocationRepository,
        private val locationTracker: LocationTracker,
        private val locationNameResolver: LocationNameResolver,
        private val generateWeatherSummaryUseCase: GenerateWeatherSummaryUseCase,
        private val cityUseCases: SavedCityUseCases,
        private val lastLocationStorage: LastLocationStorage,
        private val weatherWidgetUpdater: WeatherWidgetUpdater,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(WeatherState())
        val uiState: StateFlow<WeatherState> = _uiState.asStateFlow()

        private val _effect = MutableSharedFlow<WeatherEffect>()
        val effect: SharedFlow<WeatherEffect> = _effect.asSharedFlow()

        private var weatherLoadingJob: Job? = null
        private var aiSummaryJob: Job? = null

        init {
            observeSelectedLocation()
        }

        private fun observeSelectedLocation() {
            viewModelScope.launch {
                selectedLocationRepository
                    .observeSelectedCityId()
                    .distinctUntilChanged()
                    .collectLatest { id ->
                        if (id == null) {
                            loadWeatherForCurrentLocation()
                        } else {
                            val city = cityUseCases.getCity(id)
                            if (city != null) {
                                loadWeatherForCity(
                                    lat = city.latitude,
                                    lon = city.longitude,
                                    name = city.name,
                                    id = city.id,
                                )
                            } else {
                                selectedLocationRepository.selectCurrentLocation()
                            }
                        }
                    }
            }
        }

        private fun loadWeatherForCurrentLocation() {
            weatherLoadingJob?.cancel()
            aiSummaryJob?.cancel()
            weatherLoadingJob =
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            errorMessage = null,
                            selectedCityId = null,
                            screenMode = WeatherScreenMode.Weather,
                        )
                    }
                    locationTracker.getCurrentLocation()?.let { location ->
                        val name = locationNameResolver.getLocationName(location.latitude, location.longitude)
                        lastLocationStorage.save(location.latitude, location.longitude, name)
                        fetchWeather(location.latitude, location.longitude, name ?: "Current location", null)
                    }
                        ?: run {
                            _uiState.update { it.copy(isLoading = false, errorMessage = "Location unavailable.") }
                            _effect.emit(WeatherEffect.RequestLocationPermission)
                        }
                }
        }

        private fun loadWeatherForCity(
            lat: Double,
            lon: Double,
            name: String,
            id: Int,
        ) {
            weatherLoadingJob?.cancel()
            aiSummaryJob?.cancel()
            weatherLoadingJob =
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            locationName = name,
                            screenMode = WeatherScreenMode.Weather,
                            isLoading = true,
                            errorMessage = null,
                            selectedCityId = id,
                        )
                    }
                    fetchWeather(lat, lon, name, id)
                }
        }

        private suspend fun fetchWeather(
            lat: Double,
            lon: Double,
            locationName: String,
            cityId: Int?,
        ) {
            when (val result = getWeatherUseCase(lat, lon)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            locationName = locationName,
                            aiSummary = null,
                            aiErrorMessage = null,
                            isAiLoading = true,
                            lastUpdated = System.currentTimeMillis(),
                            selectedCityId = cityId,
                        )
                    }
                    generateAiSummary(result.data, locationName, lat, lon)
                    weatherWidgetUpdater.update()
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toMessage()) }
                    weatherWidgetUpdater.update()
                }
            }
        }

        private fun generateAiSummary(
            weatherInfo: WeatherInfo,
            locationName: String,
            lat: Double,
            lon: Double,
        ) {
            aiSummaryJob?.cancel()
            aiSummaryJob =
                viewModelScope.launch {
                    try {
                        val summary = generateWeatherSummaryUseCase(weatherInfo, locationName, lat, lon)
                        _uiState.update { it.copy(aiSummary = summary, isAiLoading = false) }
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        _uiState.update { it.copy(isAiLoading = false, aiErrorMessage = "Couldn't generate weather recommendation.") }
                    }
                }
        }

        fun onAction(action: WeatherAction) {
            when (action) {
                WeatherAction.SearchCityClicked -> _uiState.update { it.copy(screenMode = WeatherScreenMode.SearchCity) }
                WeatherAction.ManageCitiesClicked -> _uiState.update { it.copy(screenMode = WeatherScreenMode.ManageCities) }
                WeatherAction.SettingsClicked -> _uiState.update { it.copy(screenMode = WeatherScreenMode.Settings) }
                WeatherAction.BackClicked -> _uiState.update { it.copy(screenMode = WeatherScreenMode.Weather) }
                WeatherAction.Retry, WeatherAction.Refresh -> {
                    val id = _uiState.value.selectedCityId
                    if (id == null) {
                        loadWeatherForCurrentLocation()
                    } else {
                        viewModelScope.launch {
                            val city = cityUseCases.getCity(id)
                            if (city != null) {
                                loadWeatherForCity(
                                    lat = city.latitude,
                                    lon = city.longitude,
                                    name = city.name,
                                    id = city.id,
                                )
                            } else {
                                selectedLocationRepository.selectCurrentLocation()
                            }
                        }
                    }
                }
                WeatherAction.LocationPermissionGranted -> loadWeatherForCurrentLocation()
                WeatherAction.LocationPermissionDenied -> _uiState.update { it.copy(errorMessage = "Location permission is required.") }
                WeatherAction.ErrorDismissed -> _uiState.update { it.copy(errorMessage = null) }
                WeatherAction.RequestLocationPermission -> {
                    viewModelScope.launch { _effect.emit(WeatherEffect.RequestLocationPermission) }
                }
            }
        }
    }
