package com.plcoding.weatherapp.domain.weather

import androidx.annotation.DrawableRes
import com.plcoding.weatherapp.R

sealed class WeatherType(
    val weatherDesc: String,
    @DrawableRes val dayIconRes: Int,
    @DrawableRes val nightIconRes: Int,
) {
    fun getIconRes(isDay: Boolean): Int = if (isDay) dayIconRes else nightIconRes

    object ClearSky : WeatherType(
        weatherDesc = "Clear sky",
        dayIconRes = R.drawable.ic_sunny,
        nightIconRes = R.drawable.ic_w_full_moon,
    )

    object MainlyClear : WeatherType(
        weatherDesc = "Mainly clear",
        dayIconRes = R.drawable.ic_w_partly_cloudy_day,
        nightIconRes = R.drawable.ic_w_clouds,
    )

    object PartlyCloudy : WeatherType(
        weatherDesc = "Partly cloudy",
        dayIconRes = R.drawable.ic_w_partly_cloudy_day,
        nightIconRes = R.drawable.ic_w_clouds,
    )

    object Overcast : WeatherType(
        weatherDesc = "Overcast",
        dayIconRes = R.drawable.ic_w_clouds,
        nightIconRes = R.drawable.ic_w_clouds,
    )

    object Foggy : WeatherType(
        weatherDesc = "Foggy",
        dayIconRes = R.drawable.ic_w_fog_day,
        nightIconRes = R.drawable.ic_w_fog_night,
    )

    object DepositingRimeFog : WeatherType(
        weatherDesc = "Depositing rime fog",
        dayIconRes = R.drawable.ic_w_fog_day,
        nightIconRes = R.drawable.ic_w_fog_night,
    )

    object LightDrizzle : WeatherType(
        weatherDesc = "Light drizzle",
        dayIconRes = R.drawable.ic_w_light_rain_day,
        nightIconRes = R.drawable.ic_w_light_rain_night,
    )

    object ModerateDrizzle : WeatherType(
        weatherDesc = "Moderate drizzle",
        dayIconRes = R.drawable.ic_w_rain,
        nightIconRes = R.drawable.ic_w_rain,
    )

    object DenseDrizzle : WeatherType(
        weatherDesc = "Dense drizzle",
        dayIconRes = R.drawable.ic_w_rain,
        nightIconRes = R.drawable.ic_w_rain,
    )

    object LightFreezingDrizzle : WeatherType(
        weatherDesc = "Slight freezing drizzle",
        dayIconRes = R.drawable.ic_w_snow_rain,
        nightIconRes = R.drawable.ic_w_snow_rain,
    )

    object DenseFreezingDrizzle : WeatherType(
        weatherDesc = "Dense freezing drizzle",
        dayIconRes = R.drawable.ic_w_snow_rain,
        nightIconRes = R.drawable.ic_w_snow_rain,
    )

    object SlightRain : WeatherType(
        weatherDesc = "Slight rain",
        dayIconRes = R.drawable.ic_w_light_rain_day,
        nightIconRes = R.drawable.ic_w_light_rain_night,
    )

    object ModerateRain : WeatherType(
        weatherDesc = "Rainy",
        dayIconRes = R.drawable.ic_w_rain,
        nightIconRes = R.drawable.ic_w_rain,
    )

    object HeavyRain : WeatherType(
        weatherDesc = "Heavy rain",
        dayIconRes = R.drawable.ic_w_heavy_rain,
        nightIconRes = R.drawable.ic_w_heavy_rain,
    )

    object HeavyFreezingRain : WeatherType(
        weatherDesc = "Heavy freezing rain",
        dayIconRes = R.drawable.ic_w_snow_rain,
        nightIconRes = R.drawable.ic_w_snow_rain,
    )

    object SlightSnowFall : WeatherType(
        weatherDesc = "Slight snow fall",
        dayIconRes = R.drawable.ic_w_ice_pellets,
        nightIconRes = R.drawable.ic_w_ice_pellets,
    )

    object ModerateSnowFall : WeatherType(
        weatherDesc = "Moderate snow fall",
        dayIconRes = R.drawable.ic_w_snow_storm,
        nightIconRes = R.drawable.ic_w_snow_storm,
    )

    object HeavySnowFall : WeatherType(
        weatherDesc = "Heavy snow fall",
        dayIconRes = R.drawable.ic_w_blizzard,
        nightIconRes = R.drawable.ic_w_blizzard,
    )

    object SnowGrains : WeatherType(
        weatherDesc = "Snow grains",
        dayIconRes = R.drawable.ic_w_ice_pellets,
        nightIconRes = R.drawable.ic_w_ice_pellets,
    )

    object SlightRainShowers : WeatherType(
        weatherDesc = "Slight rain showers",
        dayIconRes = R.drawable.ic_w_partly_cloudy_rain,
        nightIconRes = R.drawable.ic_w_rain,
    )

    object ModerateRainShowers : WeatherType(
        weatherDesc = "Moderate rain showers",
        dayIconRes = R.drawable.ic_w_rain,
        nightIconRes = R.drawable.ic_w_rain,
    )

    object ViolentRainShowers : WeatherType(
        weatherDesc = "Violent rain showers",
        dayIconRes = R.drawable.ic_w_storm_heavy_rain,
        nightIconRes = R.drawable.ic_w_storm_heavy_rain,
    )

    object SlightSnowShowers : WeatherType(
        weatherDesc = "Light snow showers",
        dayIconRes = R.drawable.ic_w_snow_rain,
        nightIconRes = R.drawable.ic_w_snow_rain,
    )

    object HeavySnowShowers : WeatherType(
        weatherDesc = "Heavy snow showers",
        dayIconRes = R.drawable.ic_w_snow_storm,
        nightIconRes = R.drawable.ic_w_snow_storm,
    )

    object ModerateThunderstorm : WeatherType(
        weatherDesc = "Moderate thunderstorm",
        dayIconRes = R.drawable.ic_w_thunder,
        nightIconRes = R.drawable.ic_w_thunder,
    )

    object SlightHailThunderstorm : WeatherType(
        weatherDesc = "Thunderstorm with slight hail",
        dayIconRes = R.drawable.ic_w_thunder,
        nightIconRes = R.drawable.ic_w_thunder,
    )

    object HeavyHailThunderstorm : WeatherType(
        weatherDesc = "Thunderstorm with heavy hail",
        dayIconRes = R.drawable.ic_w_thunder,
        nightIconRes = R.drawable.ic_w_thunder,
    )

    companion object {
        fun fromWMO(code: Int): WeatherType =
            when (code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloudy
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSky
            }
    }
}
