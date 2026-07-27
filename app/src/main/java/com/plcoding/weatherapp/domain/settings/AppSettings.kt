package com.plcoding.weatherapp.domain.settings

data class AppSettings(
    val temperatureUnit: TemperatureUnit =
        TemperatureUnit.Celsius,
    val windSpeedUnit: WindSpeedUnit =
        WindSpeedUnit.KilometersPerHour,
    val pressureUnit: PressureUnit =
        PressureUnit.Hectopascal,
    val precipitationUnit: PrecipitationUnit =
        PrecipitationUnit.Millimeter,
    val timeFormat: AppTimeFormat =
        AppTimeFormat.SystemDefault,
    val themeMode: AppThemeMode =
        AppThemeMode.SystemDefault,
    val accentColor: AppAccentColor =
        AppAccentColor.Green,
)
