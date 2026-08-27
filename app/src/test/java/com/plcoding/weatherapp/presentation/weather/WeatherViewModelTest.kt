package com.plcoding.weatherapp.presentation.weather

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.location.LocationNameResolver
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.SelectedLocationRepository
import com.plcoding.weatherapp.domain.usecase.GenerateWeatherSummaryUseCase
import com.plcoding.weatherapp.domain.usecase.GetWeatherUseCase
import com.plcoding.weatherapp.domain.usecase.SavedCityUseCases
import com.plcoding.weatherapp.domain.util.DataError
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.presentation.weather.WeatherAction.*
import com.plcoding.weatherapp.presentation.weather.state.WeatherScreenMode
import io.mockk.coEvery
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
class WeatherViewModelTest {
    private lateinit var viewModel: WeatherViewModel
    private val getWeatherUseCase: GetWeatherUseCase = mockk()
    private val cityUseCases: SavedCityUseCases = mockk(relaxed = true)
    private val selectedLocationRepository: SelectedLocationRepository = mockk(relaxed = true)
    private val locationTracker: LocationTracker = mockk(relaxed = true)
    private val locationNameResolver: LocationNameResolver = mockk(relaxed = true)
    private val generateWeatherSummaryUseCase: GenerateWeatherSummaryUseCase = mockk(relaxed = true)
    private val lastLocationStorage: com.plcoding.weatherapp.data.preferences.LastLocationStorage = mockk(relaxed = true)
    private val weatherWidgetUpdater: com.plcoding.weatherapp.presentation.widget.WeatherWidgetUpdater = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()
    private val selectedCityIdFlow = MutableStateFlow<Int?>(null)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Mocking mandatory startup flows
        every { selectedLocationRepository.observeSelectedCityId() } returns selectedCityIdFlow

        // Mock current location behavior (triggered during init)
        coEvery { locationTracker.getCurrentLocation() } returns null

        viewModel =
            WeatherViewModel(
                getWeatherUseCase,
                selectedLocationRepository,
                locationTracker,
                locationNameResolver,
                generateWeatherSummaryUseCase,
                cityUseCases,
                lastLocationStorage,
                weatherWidgetUpdater,
            )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Initial state is correct`() =
        runTest {
            val state = viewModel.uiState.value
            assertThat(state.screenMode).isEqualTo(WeatherScreenMode.Weather)
            assertThat(state.isLoading).isFalse()
        }

    @Test
    fun `Action SearchCityClicked updates screen mode`() =
        runTest {
            viewModel.uiState.test {
                // Skip initial state from init
                awaitItem()

                // Act
                viewModel.onAction(SearchCityClicked)

                // Assert
                assertThat(awaitItem().screenMode).isEqualTo(WeatherScreenMode.SearchCity)
            }
        }

    @Test
    fun `BackClicked returns to weather screen`() =
        runTest {
            viewModel.onAction(SearchCityClicked)
            viewModel.onAction(BackClicked)

            assertThat(viewModel.uiState.value.screenMode).isEqualTo(WeatherScreenMode.Weather)
        }

    @Test
    fun `Action Refresh calls location tracker when no city selected`() =
        runTest {
            selectedCityIdFlow.value = null
            viewModel.onAction(Refresh)

            coVerify { locationTracker.getCurrentLocation() }
        }

    @Test
    fun `Action Refresh loads city weather when city is selected`() =
        runTest {
            val city = City(1, "Berlin", 52.5, 13.4, "Germany", "Berlin", "Europe/Berlin")

            coEvery { cityUseCases.getCity(1) } returns city
            coEvery { getWeatherUseCase(any(), any()) } returns Result.Error(DataError.NoInternet)

            // Set the state
            selectedCityIdFlow.value = 1

            viewModel.onAction(Refresh)

            coVerify { getWeatherUseCase(city.latitude, city.longitude) }
        }

    @Test
    fun `Location permission granted triggers weather load`() =
        runTest {
            selectedCityIdFlow.value = null
            viewModel.onAction(LocationPermissionGranted)

            coVerify { locationTracker.getCurrentLocation() }
        }
}
