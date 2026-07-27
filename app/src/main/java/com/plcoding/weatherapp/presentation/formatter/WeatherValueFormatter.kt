package com.plcoding.weatherapp.presentation.formatter

import com.plcoding.weatherapp.domain.settings.PrecipitationUnit
import com.plcoding.weatherapp.domain.settings.PressureUnit
import com.plcoding.weatherapp.domain.settings.TemperatureUnit
import com.plcoding.weatherapp.domain.settings.WindSpeedUnit
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherValueFormatter @Inject constructor() {

    fun formatTemperature(
        celsius: Double,
        unit: TemperatureUnit
    ): String{
        return when(unit){
            TemperatureUnit.Celsius ->
                "$celsius°C"
            TemperatureUnit.Fahrenheit -> {
                val fahrenheit = celsius * 9.0 / 5.0 + 32.0
                "$fahrenheit°F"
            }
        }
    }

    fun formatWindSpeed(
        kilometersPerHour: Double,
        unit: WindSpeedUnit,
    ): String {
        return when (unit){
            WindSpeedUnit.KilometersPerHour -> "${kilometersPerHour.roundToInt()} km/h"
            WindSpeedUnit.MetersPerSecond -> {
                val metersPerSecond = kilometersPerHour / 3.6
                "${formatOneDecimal(metersPerSecond)} m/s"}
            WindSpeedUnit.MilesPerHour -> {
                    val milesPerHour = kilometersPerHour * 0.621371
                    "${formatOneDecimal(milesPerHour)} mph"
                }
            }
        }

    fun formatPressure(
        hectopascal: Double,
        unit: PressureUnit,
    ): String {
        return when (unit) {
            PressureUnit.Hectopascal ->
                "${hectopascal.roundToInt()} hPa"
            PressureUnit.MillimetersOfMercury -> {
                val millimetersOfMercury = hectopascal * 0.750062
                "${millimetersOfMercury.roundToInt()} mmHg"
            }
            PressureUnit.InchesOfMercury -> {
                val inchesOfMercury = hectopascal * 0.02953
                "${formatTwoDecimals(inchesOfMercury)} inHg"
            }
        }
    }

    fun formatPrecipitation(
        millimeters: Double,
        unit: PrecipitationUnit,
    ): String {
        return when (unit) {
            PrecipitationUnit.Millimeter ->
                "${formatOneDecimal(millimeters)} mm"
            PrecipitationUnit.Inch -> {
                val inches = millimeters / 25.4
                "${formatTwoDecimals(inches)} in"
            }
        }
    }


    private fun formatOneDecimal(value: Double): String =
        "%.1f".format(value)

    private fun formatTwoDecimals(value: Double): String =
        "%.2f".format(value)


}





