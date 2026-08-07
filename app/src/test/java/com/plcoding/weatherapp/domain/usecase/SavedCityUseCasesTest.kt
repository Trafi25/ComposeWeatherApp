package com.plcoding.weatherapp.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.repository.SavedCityRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SavedCityUseCasesTest {

    private lateinit var savedCityUseCases: SavedCityUseCases
    private val repository: SavedCityRepository = mockk()

    @BeforeEach
    fun setUp() {
        savedCityUseCases = SavedCityUseCases(
            observeSavedCities = ObserveSavedCitiesUseCase(repository),
            saveCity = SaveCityUseCase(repository),
            deleteCity = DeleteCityUseCase(repository),
            getCity = GetCityUseCase(repository)
        )
    }

    @Test
    fun `observeSavedCities returns flow from repository`() = runTest {
        val cities = listOf(mockk<City>())
        every { repository.observeSavedCities() } returns flowOf(cities)

        val result = savedCityUseCases.observeSavedCities()

        result.collect {
            assertThat(it).isEqualTo(cities)
        }
    }

    @Test
    fun `saveCity calls repository`() = runTest {
        val city = mockk<City>()
        coEvery { repository.saveCity(city) } returns Unit

        savedCityUseCases.saveCity(city)

        coVerify { repository.saveCity(city) }
    }

    @Test
    fun `deleteCity calls repository`() = runTest {
        val cityId = 1
        coEvery { repository.deleteCity(cityId) } returns Unit

        savedCityUseCases.deleteCity(cityId)

        coVerify { repository.deleteCity(cityId) }
    }
}
