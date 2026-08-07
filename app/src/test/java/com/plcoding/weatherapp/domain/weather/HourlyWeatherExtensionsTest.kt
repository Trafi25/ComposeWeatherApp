package com.plcoding.weatherapp.domain.weather

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class HourlyWeatherExtensionsTest {
    @Test
    fun `upcomingHours filters out past hours and takes specified count`() {
        val now = LocalDateTime.of(2023, 1, 1, 10, 0)
        val weatherDataList =
            listOf(
                createWeatherData(LocalDateTime.of(2023, 1, 1, 8, 0)),
                createWeatherData(LocalDateTime.of(2023, 1, 1, 9, 0)),
                createWeatherData(LocalDateTime.of(2023, 1, 1, 10, 0)),
                createWeatherData(LocalDateTime.of(2023, 1, 1, 11, 0)),
                createWeatherData(LocalDateTime.of(2023, 1, 1, 12, 0)),
                createWeatherData(LocalDateTime.of(2023, 1, 1, 13, 0)),
            )

        val result = weatherDataList.upcomingHours(now = now, count = 2)

        assertThat(result).hasSize(2)
        assertThat(result[0].time).isEqualTo(LocalDateTime.of(2023, 1, 1, 10, 0))
        assertThat(result[1].time).isEqualTo(LocalDateTime.of(2023, 1, 1, 11, 0))
    }

    private fun createWeatherData(time: LocalDateTime): WeatherData =
        WeatherData(
            time = time,
            temperatureCelsius = 20.0,
            pressure = 1013.0,
            windSpeed = 10.0,
            humidity = 50.0,
            weatherType = WeatherType.ClearSky,
            isDay = true,
        )
}
