package com.plcoding.weatherapp.domain.usecase

import com.plcoding.weatherapp.domain.repository.SettingsRepository
import com.plcoding.weatherapp.domain.repository.WeatherCacheRepository
import com.plcoding.weatherapp.domain.settings.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A bundle of all settings-related Use Cases.
 */
class SettingsUseCases
    @Inject
    constructor(
        val observeSettings: ObserveSettingsUseCase,
        val setTemperatureUnit: SetTemperatureUnitUseCase,
        val setWindSpeedUnit: SetWindSpeedUnitUseCase,
        val setPressureUnit: SetPressureUnitUseCase,
        val setPrecipitationUnit: SetPrecipitationUnitUseCase,
        val setTimeFormat: SetTimeFormatUseCase,
        val setThemeMode: SetThemeModeUseCase,
        val setAccentColor: SetAccentColorUseCase,
        val clearWeatherCache: ClearWeatherCacheUseCase,
    )

class ObserveSettingsUseCase
    @Inject
    constructor(
        private val repository: SettingsRepository,
    ) {
        operator fun invoke(): Flow<AppSettings> = repository.observeSettings()
    }

class SetTemperatureUnitUseCase
    @Inject
    constructor(
        private val repository: SettingsRepository,
    ) {
        suspend operator fun invoke(unit: TemperatureUnit) = repository.setTemperatureUnit(unit)
    }

class SetWindSpeedUnitUseCase
    @Inject
    constructor(
        private val repository: SettingsRepository,
    ) {
        suspend operator fun invoke(unit: WindSpeedUnit) = repository.setWindSpeedUnit(unit)
    }

class SetPressureUnitUseCase
    @Inject
    constructor(
        private val repository: SettingsRepository,
    ) {
        suspend operator fun invoke(unit: PressureUnit) = repository.setPressureUnit(unit)
    }

class SetPrecipitationUnitUseCase
    @Inject
    constructor(
        private val repository: SettingsRepository,
    ) {
        suspend operator fun invoke(unit: PrecipitationUnit) = repository.setPrecipitationUnit(unit)
    }

class SetTimeFormatUseCase
    @Inject
    constructor(
        private val repository: SettingsRepository,
    ) {
        suspend operator fun invoke(format: AppTimeFormat) = repository.setTimeFormat(format)
    }

class SetThemeModeUseCase
    @Inject
    constructor(
        private val repository: SettingsRepository,
    ) {
        suspend operator fun invoke(mode: AppThemeMode) = repository.setThemeMode(mode)
    }

class SetAccentColorUseCase
    @Inject
    constructor(
        private val repository: SettingsRepository,
    ) {
        suspend operator fun invoke(color: AppAccentColor) = repository.setAccentColor(color)
    }

class ClearWeatherCacheUseCase
    @Inject
    constructor(
        private val repository: WeatherCacheRepository,
    ) {
        suspend operator fun invoke() = repository.clearWeatherCache()
    }
