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
    val weatherNotificationEnabled: Boolean = false,
)

fun TemperatureUnit.displayName(): String =
    when (this) {
        TemperatureUnit.Celsius -> "Celsius"
        TemperatureUnit.Fahrenheit -> "Fahrenheit"
    }

fun WindSpeedUnit.displayName(): String =
    when (this) {
        WindSpeedUnit.KilometersPerHour -> "km/h"
        WindSpeedUnit.MetersPerSecond -> "m/s"
        WindSpeedUnit.MilesPerHour -> "mph"
    }

fun PressureUnit.displayName(): String =
    when (this) {
        PressureUnit.Hectopascal -> "hPa"
        PressureUnit.MillimetersOfMercury -> "mmHg"
        PressureUnit.InchesOfMercury -> "inHg"
    }

fun PrecipitationUnit.displayName(): String =
    when (this) {
        PrecipitationUnit.Millimeter -> "Millimeter"
        PrecipitationUnit.Inch -> "Inch"
    }

fun AppTimeFormat.displayName(): String =
    when (this) {
        AppTimeFormat.SystemDefault -> "System Default"
        AppTimeFormat.TwentyFourHour -> "24-hour"
        AppTimeFormat.TwelveHour -> "12-hour"
    }

fun AppThemeMode.displayName(): String =
    when (this) {
        AppThemeMode.SystemDefault -> "System Default"
        AppThemeMode.Light -> "Light"
        AppThemeMode.Dark -> "Dark"
    }

fun AppAccentColor.displayName(): String =
    when (this) {
        AppAccentColor.Green -> "Green"
        AppAccentColor.Blue -> "Blue"
        AppAccentColor.Red -> "Red"
    }
