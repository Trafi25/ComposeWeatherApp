package com.plcoding.weatherapp.data.mappers

import com.plcoding.weatherapp.data.remote.CurrentWeatherDto
import com.plcoding.weatherapp.data.remote.WeatherDataDto
import com.plcoding.weatherapp.data.remote.WeatherDto
import com.plcoding.weatherapp.domain.weather.CurrentWeatherData
import com.plcoding.weatherapp.domain.weather.WeatherData
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import com.plcoding.weatherapp.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData,
)

fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> =
    time
        .mapIndexed { index, time ->
            val temperature = temperatures[index]
            val weatherCode = weatherCodes[index]
            val windSpeed = windSpeeds[index]
            val pressure = pressures[index]
            val humidity = humidities[index]
            val isDay = isDays[index] == 1
            IndexedWeatherData(
                index = index,
                data =
                    WeatherData(
                        time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                        temperatureCelsius = temperature,
                        pressure = pressure,
                        windSpeed = windSpeed,
                        humidity = humidity,
                        weatherType = WeatherType.fromWMO(weatherCode),
                        isDay = isDay,
                    ),
            )
        }.groupBy {
            it.index / 24
        }.mapValues {
            it.value.map { it.data }
        }

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap =
        hourlyWeatherData.toWeatherDataMap()
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData.toWeatherData(),
    )
}

private fun CurrentWeatherDto.toWeatherData(): CurrentWeatherData =
    CurrentWeatherData(
        time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
        temperatureCelsius = temperature,
        apparentTemperatureCelsius = apparentTemperature,
        humidity = humidity,
        pressure = pressure,
        precipitationMm = precipitation,
        cloudCoverPercent = cloudCover,
        windSpeed = windSpeed,
        windDirectionDegrees = windDirection,
        windDirectionLabel = windDirection.toCompareDirection(),
        windGustsKmh = windGusts,
        isDay = isDay == 1,
        weatherType =
            WeatherType.fromWMO(
                code = weatherCode,
            ),
    )

fun Int.toCompareDirection(): String {
    val normalizedDegrees = ((this % 360) + 360) % 360

    return when (normalizedDegrees) {
        in 338..359, in 0..22 -> "N"
        in 23..67 -> "NE"
        in 68..112 -> "E"
        in 113..157 -> "SE"
        in 158..202 -> "S"
        in 203..247 -> "SW"
        in 248..292 -> "W"
        in 293..337 -> "NW"
        else -> "N"
    }
}
