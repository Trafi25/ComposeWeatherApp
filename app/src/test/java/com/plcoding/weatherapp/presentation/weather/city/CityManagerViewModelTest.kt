package com.plcoding.weatherapp.presentation.weather.city

import com.plcoding.weatherapp.domain.repository.SelectedLocationRepository
import com.plcoding.weatherapp.domain.usecase.SavedCityUseCases
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CityManagerViewModelTest {
    private lateinit var viewModel: CityManagerViewModel
    private val cityUseCases: SavedCityUseCases = mockk(relaxed = true)
    private val selectedLocationRepository: SelectedLocationRepository = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { cityUseCases.observeSavedCities() } returns flowOf(emptyList())
        every { selectedLocationRepository.observeSelectedCityId() } returns flowOf(null)
        viewModel = CityManagerViewModel(cityUseCases, selectedLocationRepository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `CurrentLocationSelected updates repository`() =
        runTest {
            viewModel.onAction(CityManagerAction.CurrentLocationSelected)

            coVerify { selectedLocationRepository.selectCurrentLocation() }
        }

    @Test
    fun `CityDeleted calls use case`() =
        runTest {
            viewModel.onAction(CityManagerAction.CityDeleted(1))

            coVerify { cityUseCases.deleteCity(1) }
        }
}
