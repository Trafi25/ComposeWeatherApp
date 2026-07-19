package com.plcoding.weatherapp.data.mappers

import com.plcoding.weatherapp.data.remote.CurrentWeatherDto
import com.plcoding.weatherapp.data.remote.WeatherDataDto
import com.plcoding.weatherapp.data.remote.WeatherDto
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

private fun CurrentWeatherDto.toWeatherData(): WeatherData =
    WeatherData(
        time = LocalDateTime.parse(time),
        temperatureCelsius = temperature,
        pressure = pressure,
        windSpeed = windSpeed,
        humidity = humidity,
        weatherType = WeatherType.fromWMO(weatherCode),
        isDay = isDay == 1,
    )
