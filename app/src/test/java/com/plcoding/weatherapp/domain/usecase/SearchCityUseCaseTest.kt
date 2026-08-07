package com.plcoding.weatherapp.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.repository.CityRepository
import com.plcoding.weatherapp.domain.usecase.SearchCityUseCase
import com.plcoding.weatherapp.domain.util.DataError
import com.plcoding.weatherapp.domain.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchCityUseCaseTest {

    private lateinit var searchCityUseCase: SearchCityUseCase
    private lateinit var cityRepository: CityRepository

    @BeforeEach
    fun setUp() {
        cityRepository = mockk()
        searchCityUseCase = SearchCityUseCase(cityRepository)
    }

    @Test
    fun `Query shorter than 2 characters returns InvalidQuery error`() = runTest {
        val query = "a"
        val result = searchCityUseCase(query)

        assertThat(result is Result.Error).isTrue()
        val error = (result as Result.Error).error
        assertThat(error).isEqualTo(DataError.InvalidQuery)
    }

    @Test
    fun `Valid query returns success from repository`() = runTest {
        // Arrange
        val query = "Berlin"
        val cities = listOf(City(id = 1, name = "Berlin", latitude = 52.5, longitude = 13.4, country = "Germany", adminArea = "Berlin", timezone = "Europe/Berlin"))
        coEvery { cityRepository.searchCities(query) } returns Result.Success(cities)

        // Act
        val result = searchCityUseCase(query)

        // Assert
        assertThat(result is Result.Success).isTrue()
        assertThat((result as Result.Success).data).isEqualTo(cities)
    }
}
