package com.plcoding.weatherapp.presentation.weather.settings

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.domain.settings.TemperatureUnit
import com.plcoding.weatherapp.domain.usecase.SettingsUseCases
import com.plcoding.weatherapp.presentation.weather.state.WeatherEffect
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    private lateinit var viewModel: SettingsViewModel
    private val settingsUseCases: SettingsUseCases = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()
    private val settingsFlow = MutableStateFlow(AppSettings())

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { settingsUseCases.observeSettings() } returns settingsFlow
        viewModel = SettingsViewModel(settingsUseCases)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Settings are observed on init`() =
        runTest {
            val newSettings = AppSettings(temperatureUnit = TemperatureUnit.Fahrenheit)
            settingsFlow.value = newSettings

            assertThat(viewModel.settings.value).isEqualTo(newSettings)
        }

    @Test
    fun `ToggleTemperatureUnit calls use case`() =
        runTest {
            settingsFlow.value = AppSettings(temperatureUnit = TemperatureUnit.Celsius)

            viewModel.onAction(SettingsAction.ToggleTemperatureUnit)

            coVerify { settingsUseCases.setTemperatureUnit(TemperatureUnit.Fahrenheit) }
        }

    @Test
    fun `ToggleNotifications emits RequestNotificationPermission effect when enabled`() =
        runTest {
            settingsFlow.value = AppSettings(weatherNotificationEnabled = false)

            viewModel.effect.test {
                viewModel.onAction(SettingsAction.ToggleNotifications)
                assertThat(awaitItem()).isEqualTo(WeatherEffect.RequestNotificationPermission)
            }
        }
}
