package com.plcoding.weatherapp.data.remote

import com.google.common.truth.Truth.assertThat
import com.plcoding.weatherapp.data.remote.dtos.WeatherDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class WeatherApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: WeatherApi

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(WeatherApi::class.java)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getWeatherData returns correctly parsed DTO`() = runTest {
        val json = """
            {
              "current": {
                "time": "2023-01-01T12:00",
                "temperature_2m": 20.0,
                "apparent_temperature": 18.0,
                "relative_humidity_2m": 50,
                "pressure_msl": 1013.0,
                "precipitation": 0.0,
                "cloud_cover": 10,
                "wind_speed_10m": 10.0,
                "wind_direction_10m": 180,
                "wind_gusts_10m": 15.0,
                "is_day": 1,
                "weather_code": 0
              },
              "hourly": {
                "time": ["2023-01-01T12:00"],
                "temperature_2m": [20.0],
                "weather_code": [0],
                "wind_speed_10m": [10.0],
                "pressure_msl": [1013.0],
                "relative_humidity_2m": [50],
                "is_day": [1]
              },
              "daily": {
                "time": ["2023-01-01"],
                "weather_code": [0],
                "temperature_2m_max": [25.0],
                "temperature_2m_min": [15.0],
                "precipitation_probability_max": [10]
              }
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(json).setResponseCode(200))

        val result = api.getWeatherData(0.0, 0.0)

        assertThat(result.currentWeatherData.temperature).isEqualTo(20.0)
        assertThat(result.hourlyWeatherData.time).hasSize(1)
        assertThat(result.dailyWeatherData.time).hasSize(1)
    }
}
