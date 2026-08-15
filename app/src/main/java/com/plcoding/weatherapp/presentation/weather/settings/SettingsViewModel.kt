package com.plcoding.weatherapp.presentation.weather.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.settings.*
import com.plcoding.weatherapp.domain.usecase.SettingsUseCases
import com.plcoding.weatherapp.presentation.weather.state.WeatherEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    private val _settings = MutableStateFlow(AppSettings())
    val settings = _settings.asStateFlow()

    private val _effect = MutableSharedFlow<WeatherEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            settingsUseCases.observeSettings().collect { newSettings ->
                _settings.update { newSettings }
            }
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.ToggleTemperatureUnit -> toggleTemperatureUnit()
            SettingsAction.ToggleWindSpeedUnit -> toggleWindSpeedUnit()
            SettingsAction.TogglePressureUnit -> togglePressureUnit()
            SettingsAction.TogglePrecipitationUnit -> togglePrecipitationUnit()
            SettingsAction.ToggleTimeFormat -> toggleTimeFormat()
            SettingsAction.ToggleThemeMode -> toggleThemeMode()
            is SettingsAction.AccentColorSelected -> setAccentColor(action.color)
            SettingsAction.ToggleNotifications -> toggleNotifications()
            SettingsAction.ClearCache -> clearCache()
            SettingsAction.BackClicked -> { /* Handled by WeatherViewModel */ }
        }
    }

    private fun toggleTemperatureUnit() {
        val next = if (_settings.value.temperatureUnit == TemperatureUnit.Celsius)
            TemperatureUnit.Fahrenheit else TemperatureUnit.Celsius
        updateSetting { settingsUseCases.setTemperatureUnit(next) }
    }

    private fun toggleWindSpeedUnit() = toggle(WindSpeedUnit.entries, _settings.value.windSpeedUnit) { settingsUseCases.setWindSpeedUnit(it) }
    private fun togglePressureUnit() = toggle(PressureUnit.entries, _settings.value.pressureUnit) { settingsUseCases.setPressureUnit(it) }
    private fun togglePrecipitationUnit() = toggle(PrecipitationUnit.entries, _settings.value.precipitationUnit) { settingsUseCases.setPrecipitationUnit(it) }
    private fun toggleTimeFormat() = toggle(AppTimeFormat.entries, _settings.value.timeFormat) { settingsUseCases.setTimeFormat(it) }
    private fun toggleThemeMode() = toggle(AppThemeMode.entries, _settings.value.themeMode) { settingsUseCases.setThemeMode(it) }

    private fun setAccentColor(color: AppAccentColor) = updateSetting { settingsUseCases.setAccentColor(color) }

    private fun toggleNotifications() {
        viewModelScope.launch {
            val next = !_settings.value.weatherNotificationEnabled
            if (next) _effect.emit(WeatherEffect.RequestNotificationPermission)
            settingsUseCases.setNotificationEnabled(next)
        }
    }

    private fun clearCache() = updateSetting { settingsUseCases.clearWeatherCache() }

    private fun <T> toggle(entries: List<T>, current: T, block: suspend (T) -> Unit) {
        val next = entries[(entries.indexOf(current) + 1) % entries.size]
        updateSetting { block(next) }
    }

    private fun updateSetting(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }
}
