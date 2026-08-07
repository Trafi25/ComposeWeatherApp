package com.plcoding.weatherapp.data.mappers

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class WeatherMappersTest {

    @Test
    fun `toCompareDirection returns correct labels for various degrees`() {
        val testCases = mapOf(
            0 to "N",
            22 to "N",
            23 to "NE",
            67 to "NE",
            68 to "E",
            112 to "E",
            113 to "SE",
            157 to "SE",
            158 to "S",
            202 to "S",
            203 to "SW",
            247 to "SW",
            248 to "W",
            292 to "W",
            293 to "NW",
            337 to "NW",
            338 to "N",
            359 to "N",
            360 to "N",
            -45 to "NW",
            405 to "NE"
        )

        testCases.forEach { (degrees, expected) ->
            assertThat(degrees.toCompareDirection()).isEqualTo(expected)
        }
    }
}
