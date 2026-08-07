package com.plcoding.weatherapp.presentation.formatter

import com.google.common.truth.Truth.assertThat
import com.plcoding.weatherapp.domain.settings.TemperatureUnit
import com.plcoding.weatherapp.domain.settings.WindSpeedUnit
import org.junit.jupiter.api.Test
import java.util.Locale

class WeatherValueFormatterTest {
    private val formatter = WeatherValueFormatter()

    @Test
    fun `formatTemperature returns correctly formatted string for Celsius`() {
        val result = formatter.formatTemperature(25.0, TemperatureUnit.Celsius)
        assertThat(result).isEqualTo("25.0°C")
    }

    @Test
    fun `formatTemperature returns correctly formatted string for Fahrenheit`() {
        val result = formatter.formatTemperature(20.0, TemperatureUnit.Fahrenheit)
        assertThat(result).isEqualTo("68.0°F")
    }

    @Test
    fun `formatWindSpeed returns correctly formatted string for km-h`() {
        val result = formatter.formatWindSpeed(10.0, WindSpeedUnit.KilometersPerHour)
        assertThat(result).isEqualTo("10 km/h")
    }

    @Test
    fun `formatWindSpeed returns correctly formatted string for m-s`() {
        Locale.setDefault(Locale.US)
        val result = formatter.formatWindSpeed(36.0, WindSpeedUnit.MetersPerSecond)
        assertThat(result).isEqualTo("10.0 m/s")
    }
}
