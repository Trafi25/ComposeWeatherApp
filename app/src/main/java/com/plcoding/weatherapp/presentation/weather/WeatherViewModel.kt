package com.plcoding.weatherapp.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.location.LocationNameResolver
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.SelectedLocationRepository
import com.plcoding.weatherapp.domain.settings.*
import com.plcoding.weatherapp.domain.usecase.GetWeatherUseCase
import com.plcoding.weatherapp.domain.usecase.SavedCityUseCases
import com.plcoding.weatherapp.domain.usecase.SearchCityUseCase
import com.plcoding.weatherapp.domain.usecase.SettingsUseCases
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.util.toMessage
import com.plcoding.weatherapp.presentation.weather.state.CitySearchState
import com.plcoding.weatherapp.presentation.weather.state.WeatherScreenMode
import com.plcoding.weatherapp.presentation.weather.state.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
    @Inject
    constructor(
        private val getWeatherUseCase: GetWeatherUseCase,
        private val searchCityUseCase: SearchCityUseCase,
        private val cityUseCases: SavedCityUseCases,
        private val settingsUseCases: SettingsUseCases,
        private val selectedLocationRepository: SelectedLocationRepository,
        private val locationTracker: LocationTracker,
        private val locationNameResolver: LocationNameResolver,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(WeatherState())
        val uiState: StateFlow<WeatherState> = _uiState.asStateFlow()

        private var citySearchJob: Job? = null

        init {
            observeSettings()
            observeSavedCities()
            restoreSelectedLocation()
        }

        private companion object {
            const val MIN_CITY_QUERY_LENGTH = 2
            const val CITY_SEARCH_DEBOUNCE = 500L
        }

        private fun observeSavedCities() {
            viewModelScope.launch {
                cityUseCases.observeSavedCities().collect { cities ->
                    _uiState.update { it.copy(savedCities = cities) }
                }
            }
        }

        private fun restoreSelectedLocation() {
            viewModelScope.launch {
                val selectedCityId = selectedLocationRepository.observeSelectedCityId().first()
                if (selectedCityId == null) {
                    _uiState.update { it.copy(selectedCityId = null, isLocationRestored = true) }
                    loadWeatherInfo()
                    return@launch
                }
                val city = cityUseCases.getCity(selectedCityId)
                if (city != null) {
                    _uiState.update { it.copy(selectedCityId = city.id, isLocationRestored = true) }
                    loadWeatherForCity(city = city, savedSelection = false)
                } else {
                    selectedLocationRepository.selectCurrentLocation()
                    _uiState.update { it.copy(selectedCityId = null, isLocationRestored = true) }
                    loadWeatherInfo()
                }
            }
        }

        private fun loadWeatherInfo() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, selectedCityId = null) }

                locationTracker.getCurrentLocation()?.let { location ->
                    when (val result = getWeatherUseCase(location.latitude, location.longitude)) {
                        is Result.Success -> {
                            val name = locationNameResolver.getLocationName(location.latitude, location.longitude)
                            _uiState.update { it.copy(weatherInfo = result.data, isLoading = false, locationName = name) }
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toMessage()) }
                        }
                    }
                } ?: _uiState.update { it.copy(isLoading = false, errorMessage = "Location unavailable.") }
            }
        }

        private fun loadWeatherForCity(
            city: City,
            savedSelection: Boolean = true,
        ) {
            citySearchJob?.cancel()
            viewModelScope.launch {
                cityUseCases.saveCity(city)
                if (savedSelection) selectedLocationRepository.saveSelectedCityId(city.id)

                _uiState.update {
                    it.copy(
                        locationName = city.name,
                        screenMode = WeatherScreenMode.Weather,
                        citySearchState = CitySearchState(),
                        isLoading = true,
                        errorMessage = null,
                        selectedCityId = city.id,
                    )
                }

                when (val result = getWeatherUseCase(city.latitude, city.longitude)) {
                    is Result.Success -> _uiState.update { it.copy(weatherInfo = result.data, isLoading = false) }
                    is Result.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toMessage()) }
                }
            }
        }

        private fun updateSearchQuery(query: String) {
            _uiState.update { it.copy(citySearchState = it.citySearchState.copy(query = query, errorMessage = null)) }
            citySearchJob?.cancel()
            if (query.length < MIN_CITY_QUERY_LENGTH) {
                _uiState.update { it.copy(citySearchState = it.citySearchState.copy(results = emptyList(), isLoading = false)) }
                return
            }
            citySearchJob =
                viewModelScope.launch {
                    delay(CITY_SEARCH_DEBOUNCE)
                    searchCities(query)
                }
        }

        private suspend fun searchCities(query: String) {
            _uiState.update { it.copy(citySearchState = it.citySearchState.copy(isLoading = true)) }
            when (val result = searchCityUseCase(query)) {
                is Result.Success -> {
                    _uiState.update { state ->
                        if (state.citySearchState.query != query) {
                            state
                        } else {
                            state.copy(citySearchState = state.citySearchState.copy(results = result.data, isLoading = false))
                        }
                    }
                }
                is Result.Error -> {
                    _uiState.update { state ->
                        if (state.citySearchState.query != query) {
                            state
                        } else {
                            state.copy(
                                citySearchState =
                                    state.citySearchState.copy(
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

        fun onAction(action: WeatherAction) {
            when (action) {
                WeatherAction.SearchCityClicked ->
                    _uiState.update {
                        it.copy(screenMode = WeatherScreenMode.SearchCity, citySearchState = CitySearchState())
                    }
                WeatherAction.ManageCitiesClicked -> _uiState.update { it.copy(screenMode = WeatherScreenMode.ManageCities) }
                WeatherAction.CityScreenBackClicked -> {
                    citySearchJob?.cancel()
                    _uiState.update { it.copy(screenMode = WeatherScreenMode.Weather) }
                }
                WeatherAction.LoadWeather -> restoreSelectedLocation()
                WeatherAction.Retry -> retryWeatherLoading()
                WeatherAction.ErrorDismissed -> _uiState.update { it.copy(errorMessage = null) }
                WeatherAction.CurrentLocationSelected -> selectCurrentLocation()
                WeatherAction.LocationPermissionGranted -> {
                    if (_uiState.value.isLocationRestored &&
                        _uiState.value.selectedCityId == null &&
                        _uiState.value.weatherInfo == null
                    ) {
                        loadWeatherInfo()
                    }
                }
                WeatherAction.LocationPermissionDenied -> _uiState.update { it.copy(errorMessage = "Permission required.") }
                WeatherAction.SettingsClicked -> {
                    _uiState.update { it.copy(screenMode = WeatherScreenMode.Settings) }
                }
                WeatherAction.SettingsBackClicked -> {
                    _uiState.update { it.copy(screenMode = WeatherScreenMode.Weather) }
                }
                is WeatherAction.TemperatureUnitSelected -> {
                    setTemperatureUnit(action.unit)
                }
                is WeatherAction.WindSpeedUnitSelected -> {
                    setWindSpeedUnit(action.unit)
                }
                is WeatherAction.PressureUnitSelected -> {
                    setPressureUnit(action.unit)
                }
                is WeatherAction.PrecipitationUnitSelected -> {
                    setPrecipitationUnit(action.unit)
                }
                WeatherAction.ClearCacheClicked -> {
                    clearCache()
                }
                is WeatherAction.TimeFormatSelected -> {
                    setTimeFormat(action.format)
                }
                is WeatherAction.ThemeModeSelected -> {
                    setThemeMode(action.mode)
                }
                is WeatherAction.AccentColorSelected -> {
                    setAccentColor(action.color)
                }
                is WeatherAction.SearchQueryChanged -> updateSearchQuery(action.query)
                is WeatherAction.CitySelected -> loadWeatherForCity(action.city)
                is WeatherAction.SavedCityDeleted -> {
                    viewModelScope.launch {
                        cityUseCases.deleteCity(action.cityId)
                        if (_uiState.value.selectedCityId == action.cityId) selectCurrentLocation()
                    }
                }
                else -> {}
            }
        }

        private fun retryWeatherLoading() {
            val id = _uiState.value.selectedCityId ?: return loadWeatherInfo()
            viewModelScope.launch {
                cityUseCases.getCity(id)?.let { loadWeatherForCity(it, false) } ?: selectCurrentLocation()
            }
        }

        private fun selectCurrentLocation() {
            citySearchJob?.cancel()
            viewModelScope.launch { selectedLocationRepository.selectCurrentLocation() }
            _uiState.update { it.copy(selectedCityId = null, screenMode = WeatherScreenMode.Weather, errorMessage = null) }
            loadWeatherInfo()
        }

        private fun observeSettings() {
            viewModelScope.launch {
                settingsUseCases.observeSettings().collect { settings ->
                    _uiState.update { currentState ->
                        currentState.copy(appSettings = settings)
                    }
                }
            }
        }

        private fun setTemperatureUnit(unit: TemperatureUnit) {
            viewModelScope.launch { settingsUseCases.setTemperatureUnit(unit) }
        }

        private fun setWindSpeedUnit(unit: WindSpeedUnit) {
            viewModelScope.launch { settingsUseCases.setWindSpeedUnit(unit) }
        }

        private fun setPressureUnit(unit: PressureUnit) {
            viewModelScope.launch { settingsUseCases.setPressureUnit(unit) }
        }

        private fun setPrecipitationUnit(unit: PrecipitationUnit) {
            viewModelScope.launch { settingsUseCases.setPrecipitationUnit(unit) }
        }

        private fun setTimeFormat(format: AppTimeFormat) {
            viewModelScope.launch { settingsUseCases.setTimeFormat(format) }
        }

        private fun setThemeMode(mode: AppThemeMode) {
            viewModelScope.launch { settingsUseCases.setThemeMode(mode) }
        }

        private fun setAccentColor(color: AppAccentColor) {
            viewModelScope.launch { settingsUseCases.setAccentColor(color) }
        }

        private fun clearCache() {
            viewModelScope.launch {
                settingsUseCases.clearWeatherCache()
            }
        }
    }
