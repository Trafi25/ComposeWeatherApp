package com.plcoding.weatherapp.presentation.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.location.LocationNameResolver
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.SelectedLocationRepository
import com.plcoding.weatherapp.domain.settings.*
import com.plcoding.weatherapp.domain.usecase.GenerateWeatherSummaryUseCase
import com.plcoding.weatherapp.domain.usecase.GetWeatherUseCase
import com.plcoding.weatherapp.domain.usecase.SavedCityUseCases
import com.plcoding.weatherapp.domain.usecase.SearchCityUseCase
import com.plcoding.weatherapp.domain.usecase.SettingsUseCases
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.util.toMessage
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import com.plcoding.weatherapp.presentation.weather.state.CitySearchState
import com.plcoding.weatherapp.presentation.weather.state.WeatherEffect
import com.plcoding.weatherapp.presentation.weather.state.WeatherScreenMode
import com.plcoding.weatherapp.presentation.weather.state.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

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
        private val generateWeatherSummaryUseCase: GenerateWeatherSummaryUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(WeatherState())
        val uiState: StateFlow<WeatherState> = _uiState.asStateFlow()

        private val _effect = MutableSharedFlow<WeatherEffect>()
        val effect: SharedFlow<WeatherEffect> = _effect.asSharedFlow()

        private var weatherLoadingJob: Job? = null
        private var aiSummaryJob: Job? = null
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
            weatherLoadingJob?.cancel()
            aiSummaryJob?.cancel()
            weatherLoadingJob =
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null, selectedCityId = null) }

                    locationTracker.getCurrentLocation()?.let { location ->
                        val name = locationNameResolver.getLocationName(location.latitude, location.longitude)
                        fetchWeather(location.latitude, location.longitude, name ?: "Current location", null)
                    } ?: run {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Location unavailable.") }
                        _effect.emit(WeatherEffect.RequestLocationPermission)
                    }
                }
        }

        private fun loadWeatherForCity(
            city: City,
            savedSelection: Boolean = true,
        ) {
            weatherLoadingJob?.cancel()
            aiSummaryJob?.cancel()
            weatherLoadingJob =
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
                    fetchWeather(city.latitude, city.longitude, city.name, city.id)
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
                    generateAiSummary(
                        weatherInfo = result.data,
                        locationName = locationName,
                        latitude = lat,
                        longitude = lon,
                    )
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toMessage()) }
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

        private fun generateAiSummary(
            weatherInfo: WeatherInfo,
            locationName: String,
            latitude: Double,
            longitude: Double,
        ) {
            aiSummaryJob?.cancel()
            aiSummaryJob =
                viewModelScope.launch {
                    try {
                        val summary =
                            generateWeatherSummaryUseCase(
                                weatherInfo = weatherInfo,
                                locationName = locationName,
                                latitude = latitude,
                                longitude = longitude,
                            )
                        _uiState.update { currentState ->
                            currentState.copy(aiSummary = summary, isAiLoading = false)
                        }
                    } catch (exception: Exception) {
                        if (exception is CancellationException) throw exception
                        _uiState.update { currentState ->
                            Log.d("DEBUG_AI", exception.message ?: "Unknown error")
                            currentState.copy(
                                isAiLoading = false,
                                aiSummary = null,
                                aiErrorMessage = "Couldn't generate weather recommendation.",
                            )
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
                WeatherAction.Retry, WeatherAction.Refresh -> retryWeatherLoading()
                WeatherAction.ErrorDismissed -> _uiState.update { it.copy(errorMessage = null) }
                WeatherAction.CurrentLocationSelected -> selectCurrentLocation()
                WeatherAction.RequestLocationPermission -> {
                    viewModelScope.launch { _effect.emit(WeatherEffect.RequestLocationPermission) }
                }
                WeatherAction.LocationPermissionGranted -> {
                    if (_uiState.value.selectedCityId == null) {
                        loadWeatherInfo()
                    }
                }
                WeatherAction.LocationPermissionDenied -> _uiState.update { it.copy(errorMessage = "Permission required.") }
                WeatherAction.SettingsClicked -> _uiState.update { it.copy(screenMode = WeatherScreenMode.Settings) }
                WeatherAction.SettingsBackClicked -> _uiState.update { it.copy(screenMode = WeatherScreenMode.Weather) }
                is WeatherAction.TemperatureUnitSelected -> updateSetting { settingsUseCases.setTemperatureUnit(action.unit) }
                is WeatherAction.WindSpeedUnitSelected -> updateSetting { settingsUseCases.setWindSpeedUnit(action.unit) }
                is WeatherAction.PressureUnitSelected -> updateSetting { settingsUseCases.setPressureUnit(action.unit) }
                is WeatherAction.PrecipitationUnitSelected -> updateSetting { settingsUseCases.setPrecipitationUnit(action.unit) }
                is WeatherAction.TimeFormatSelected -> updateSetting { settingsUseCases.setTimeFormat(action.format) }
                is WeatherAction.ThemeModeSelected -> updateSetting { settingsUseCases.setThemeMode(action.mode) }
                is WeatherAction.AccentColorSelected -> updateSetting { settingsUseCases.setAccentColor(action.color) }
                WeatherAction.ClearCacheClicked -> viewModelScope.launch { settingsUseCases.clearWeatherCache() }
                is WeatherAction.NotificationToggleClicked -> notificationCall(action.isEnabled)
                WeatherAction.ToggleTemperatureUnitRequested -> {
                    val unit =
                        if (_uiState.value.appSettings.temperatureUnit ==
                            TemperatureUnit.Celsius
                        ) {
                            TemperatureUnit.Fahrenheit
                        } else {
                            TemperatureUnit.Celsius
                        }
                    updateSetting { settingsUseCases.setTemperatureUnit(unit) }
                }
                WeatherAction.ToggleWindSpeedUnitRequested ->
                    toggleSetting(
                        WindSpeedUnit.entries,
                        _uiState.value.appSettings.windSpeedUnit,
                    ) {
                        settingsUseCases.setWindSpeedUnit(it)
                    }
                WeatherAction.TogglePressureUnitRequested ->
                    toggleSetting(PressureUnit.entries, _uiState.value.appSettings.pressureUnit) {
                        settingsUseCases.setPressureUnit(it)
                    }
                WeatherAction.TogglePrecipitationUnitRequested ->
                    toggleSetting(
                        PrecipitationUnit.entries,
                        _uiState.value.appSettings.precipitationUnit,
                    ) {
                        settingsUseCases.setPrecipitationUnit(it)
                    }
                WeatherAction.ToggleTimeFormatRequested ->
                    toggleSetting(AppTimeFormat.entries, _uiState.value.appSettings.timeFormat) {
                        settingsUseCases.setTimeFormat(it)
                    }
                WeatherAction.ToggleThemeModeRequested ->
                    toggleSetting(AppThemeMode.entries, _uiState.value.appSettings.themeMode) {
                        settingsUseCases.setThemeMode(it)
                    }
                is WeatherAction.SearchQueryChanged -> updateSearchQuery(action.query)
                is WeatherAction.CitySelected -> loadWeatherForCity(action.city)
                is WeatherAction.SavedCityDeleted -> {
                    viewModelScope.launch {
                        cityUseCases.deleteCity(action.cityId)
                        if (_uiState.value.selectedCityId == action.cityId) selectCurrentLocation()
                    }
                }
            }
        }

        private fun <T> toggleSetting(
            entries: List<T>,
            current: T,
            updateBlock: suspend (T) -> Unit,
        ) {
            val nextIndex = (entries.indexOf(current) + 1) % entries.size
            updateSetting { updateBlock(entries[nextIndex]) }
        }

        private fun updateSetting(block: suspend () -> Unit) {
            viewModelScope.launch { block() }
        }

        private fun retryWeatherLoading() {
            val id = _uiState.value.selectedCityId

            if (id == null) {
                loadWeatherInfo()
            } else {
                viewModelScope.launch {
                    cityUseCases.getCity(id)?.let { loadWeatherForCity(it, false) } ?: selectCurrentLocation()
                }
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

        private fun notificationCall(isSelected: Boolean) {
            viewModelScope.launch {
                if (isSelected) {
                    _effect.emit(WeatherEffect.RequestNotificationPermission)
                }
                settingsUseCases.setNotificationEnabled(isSelected)
            }
        }
    }
