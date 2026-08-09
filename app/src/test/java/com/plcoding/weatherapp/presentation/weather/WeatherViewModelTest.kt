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
import com.plcoding.weatherapp.domain.usecase.SearchCityUseCase
import com.plcoding.weatherapp.domain.usecase.SettingsUseCases
import com.plcoding.weatherapp.domain.util.DataError
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import com.plcoding.weatherapp.presentation.weather.WeatherAction.CityScreenBackClicked
import com.plcoding.weatherapp.presentation.weather.WeatherAction.CitySelected
import com.plcoding.weatherapp.presentation.weather.WeatherAction.NotificationToggleClicked
import com.plcoding.weatherapp.presentation.weather.WeatherAction.Refresh
import com.plcoding.weatherapp.presentation.weather.WeatherAction.SearchCityClicked
import com.plcoding.weatherapp.presentation.weather.WeatherAction.ToggleTemperatureUnitRequested
import com.plcoding.weatherapp.presentation.weather.WeatherAction.ToggleThemeModeRequested
import com.plcoding.weatherapp.presentation.weather.state.WeatherScreenMode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
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
    private val searchCityUseCase: SearchCityUseCase = mockk()
    private val cityUseCases: SavedCityUseCases = mockk(relaxed = true)
    private val settingsUseCases: SettingsUseCases = mockk(relaxed = true)
    private val selectedLocationRepository: SelectedLocationRepository = mockk(relaxed = true)
    private val locationTracker: LocationTracker = mockk(relaxed = true)
    private val locationNameResolver: LocationNameResolver = mockk(relaxed = true)
    private val generateWeatherSummaryUseCase: GenerateWeatherSummaryUseCase = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Mocking mandatory startup flows
        every { settingsUseCases.observeSettings() } returns flowOf(mockk(relaxed = true))
        every { cityUseCases.observeSavedCities() } returns flowOf(emptyList())
        every { selectedLocationRepository.observeSelectedCityId() } returns flowOf(null)

        // Mock current location behavior (triggered during init)
        coEvery { locationTracker.getCurrentLocation() } returns null

        viewModel =
            WeatherViewModel(
                getWeatherUseCase,
                searchCityUseCase,
                cityUseCases,
                settingsUseCases,
                selectedLocationRepository,
                locationTracker,
                locationNameResolver,
                generateWeatherSummaryUseCase,
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
                // Initial state
                assertThat(awaitItem().screenMode).isEqualTo(WeatherScreenMode.Weather)

                // Act
                viewModel.onAction(SearchCityClicked)

                // Assert
                assertThat(awaitItem().screenMode).isEqualTo(WeatherScreenMode.SearchCity)
            }
        }

    @Test
    fun `CityScreenBackClicked returns to weather screen`() =
        runTest {
            viewModel.onAction(SearchCityClicked)

            viewModel.onAction(CityScreenBackClicked)

            assertThat(viewModel.uiState.value.screenMode).isEqualTo(WeatherScreenMode.Weather)
        }

    @Test
    fun `Short search query clears results and does not search`() =
        runTest {
            viewModel.onAction(WeatherAction.SearchQueryChanged("B"))

            val searchState = viewModel.uiState.value.citySearchState

            assertThat(searchState.query).isEqualTo("B")
            assertThat(searchState.results).isEmpty()
        }

    @Test
    fun `Valid search query updates city results`() =
        runTest(testDispatcher) {
            val cities =
                listOf(
                    City(
                        id = 1,
                        name = "Berlin",
                        latitude = 52.5,
                        longitude = 13.4,
                        country = "Germany",
                        adminArea = "Berlin",
                        timezone = "Europe/Berlin",
                    ),
                )

            coEvery { searchCityUseCase("Berlin") } returns Result.Success(cities)
            viewModel.onAction(WeatherAction.SearchQueryChanged("Berlin"))

            advanceTimeBy(500)
            advanceUntilIdle()

            val searchState = viewModel.uiState.value.citySearchState

            assertThat(searchState.results).isEqualTo(cities)
            assertThat(searchState.isLoading).isFalse()
            assertThat(searchState.errorMessage).isNull()
        }

    @Test
    fun `Search error updates city search error`() =
        runTest(testDispatcher) {
            coEvery { searchCityUseCase("Berlin") } returns Result.Error(DataError.NoInternet)

            viewModel.onAction(WeatherAction.SearchQueryChanged("Berlin"))

            advanceTimeBy(500)
            advanceUntilIdle()

            val searchState = viewModel.uiState.value.citySearchState
            assertThat(searchState.errorMessage).isNotNull()
            assertThat(searchState.results).isEmpty()
        }

    @Test
    fun `Action Refresh calls retryWeatherLoading`() =
        runTest {
            viewModel.onAction(Refresh)

            coVerify { locationTracker.getCurrentLocation() }
        }

    @Test
    fun `Action CitySelected loads weather for city`() =
        runTest {
            val city = City(1, "Berlin", 52.5, 13.4, "Germany", "Berlin", "Europe/Berlin")
            coEvery { getWeatherUseCase(any(), any()) } returns Result.Error(DataError.NoInternet)

            viewModel.onAction(CitySelected(city))

            assertThat(viewModel.uiState.value.locationName).isEqualTo("Berlin")
            coVerify { getWeatherUseCase(city.latitude, city.longitude) }
        }

    @Test
    fun `Action CitySelected triggers AI summary generation`() =
        runTest {
            val city = City(1, "Berlin", 52.5, 13.4, "Germany", "Berlin", "Europe/Berlin")
            val weatherInfo = mockk<WeatherInfo>(relaxed = true)
            coEvery { getWeatherUseCase(any(), any()) } returns Result.Success(weatherInfo)

            viewModel.onAction(CitySelected(city))

            coVerify { generateWeatherSummaryUseCase(weatherInfo, "Berlin") }
        }

    @Test
    fun `Action ToggleTemperatureUnitRequested calls setTemperatureUnit`() =
        runTest {
            viewModel.onAction(ToggleTemperatureUnitRequested)

            coVerify { settingsUseCases.setTemperatureUnit(any()) }
        }

    @Test
    fun `Action ToggleThemeModeRequested calls setThemeMode`() =
        runTest {
            viewModel.onAction(ToggleThemeModeRequested)

            coVerify { settingsUseCases.setThemeMode(any()) }
        }

    @Test
    fun `Action NotificationToggleClicked calls setNotificationEnabled`() =
        runTest {
            viewModel.onAction(NotificationToggleClicked(true))

            coVerify { settingsUseCases.setNotificationEnabled(true) }
        }
}
