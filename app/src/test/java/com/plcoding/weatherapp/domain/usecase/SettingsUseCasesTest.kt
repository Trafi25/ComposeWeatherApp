package com.plcoding.weatherapp.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.plcoding.weatherapp.domain.repository.SettingsRepository
import com.plcoding.weatherapp.domain.repository.WeatherCacheRepository
import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.domain.settings.TemperatureUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SettingsUseCasesTest {

    private lateinit var settingsUseCases: SettingsUseCases
    private val settingsRepository: SettingsRepository = mockk()
    private val cacheRepository: WeatherCacheRepository = mockk()

    @BeforeEach
    fun setUp() {
        settingsUseCases = SettingsUseCases(
            observeSettings = ObserveSettingsUseCase(settingsRepository),
            setTemperatureUnit = SetTemperatureUnitUseCase(settingsRepository),
            setWindSpeedUnit = SetWindSpeedUnitUseCase(settingsRepository),
            setPressureUnit = SetPressureUnitUseCase(settingsRepository),
            setPrecipitationUnit = SetPrecipitationUnitUseCase(settingsRepository),
            setTimeFormat = SetTimeFormatUseCase(settingsRepository),
            setThemeMode = SetThemeModeUseCase(settingsRepository),
            setAccentColor = SetAccentColorUseCase(settingsRepository),
            clearWeatherCache = ClearWeatherCacheUseCase(cacheRepository)
        )
    }

    @Test
    fun `observeSettings returns flow from repository`() = runTest {
        val settings = AppSettings()
        every { settingsRepository.observeSettings() } returns flowOf(settings)

        val result = settingsUseCases.observeSettings()

        result.collect {
            assertThat(it).isEqualTo(settings)
        }
    }

    @Test
    fun `setTemperatureUnit calls repository`() = runTest {
        val unit = TemperatureUnit.Fahrenheit
        coEvery { settingsRepository.setTemperatureUnit(unit) } returns Unit

        settingsUseCases.setTemperatureUnit(unit)

        coVerify { settingsRepository.setTemperatureUnit(unit) }
    }

    @Test
    fun `clearWeatherCache calls cache repository`() = runTest {
        coEvery { cacheRepository.clearWeatherCache() } returns Unit

        settingsUseCases.clearWeatherCache()

        coVerify { cacheRepository.clearWeatherCache() }
    }
}
