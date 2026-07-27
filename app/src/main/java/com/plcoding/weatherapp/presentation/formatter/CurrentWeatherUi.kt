package com.plcoding.weatherapp.presentation.formatter

import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.domain.weather.CurrentWeatherData

data class CurrentWeatherUi(
    val temperature: String,
    val apparentTemperature: String,
    val windSpeed: String,
    val pressure: String,
    val precipitation: String,
)

fun CurrentWeatherData.toUi(
    settings: AppSettings,
    formatter: WeatherValueFormatter,
): CurrentWeatherUi =
    CurrentWeatherUi(
        temperature =
            formatter.formatTemperature(
                celsius = temperatureCelsius,
                unit = settings.temperatureUnit,
            ),
        apparentTemperature =
            formatter.formatTemperature(
                celsius = apparentTemperatureCelsius,
                unit = settings.temperatureUnit,
            ),
        windSpeed =
            formatter.formatWindSpeed(
                kilometersPerHour = windSpeed,
                unit = settings.windSpeedUnit,
            ),
        pressure =
            formatter.formatPressure(
                hectopascal = pressure,
                unit = settings.pressureUnit,
            ),
        precipitation =
            formatter.formatPrecipitation(
                millimeters = precipitationMm,
                unit = settings.precipitationUnit,
            ),
    )
