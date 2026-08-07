package com.plcoding.weatherapp.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class
GetWeatherUseCaseTest {
    private lateinit var getWeatherUseCase: GetWeatherUseCase
    private lateinit var weatherRepository: WeatherRepository

    @BeforeEach
    fun setUp() {
        weatherRepository = mockk()
        getWeatherUseCase = GetWeatherUseCase(weatherRepository)
    }

    @Test
    fun `UseCase calls repository with correct coordinates`() =
        runTest {
            // Arrange
            val lat = 52.5
            val long = 13.4
            val expectedWeatherInfo = mockk<WeatherInfo>()
            coEvery { weatherRepository.getWeatherData(lat, long) } returns Result.Success(expectedWeatherInfo)

            // Act
            val result = getWeatherUseCase(lat, long)

            // Assert
            assertThat(result is Result.Success).isTrue()
            assertThat((result as Result.Success).data).isEqualTo(expectedWeatherInfo)
        }
}
