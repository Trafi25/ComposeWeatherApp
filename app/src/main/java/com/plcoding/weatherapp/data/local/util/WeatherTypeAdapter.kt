package com.plcoding.weatherapp.data.local.util

import com.plcoding.weatherapp.domain.weather.WeatherType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class WeatherTypeAdapter {
    @ToJson
    fun toJson(weatherType: WeatherType): Int =
        when (weatherType) {
            is WeatherType.ClearSky -> 0
            is WeatherType.MainlyClear -> 1
            is WeatherType.PartlyCloudy -> 2
            is WeatherType.Overcast -> 3
            is WeatherType.Foggy -> 45
            is WeatherType.DepositingRimeFog -> 48
            is WeatherType.LightDrizzle -> 51
            is WeatherType.ModerateDrizzle -> 53
            is WeatherType.DenseDrizzle -> 55
            is WeatherType.LightFreezingDrizzle -> 56
            is WeatherType.DenseFreezingDrizzle -> 57
            is WeatherType.SlightRain -> 61
            is WeatherType.ModerateRain -> 63
            is WeatherType.HeavyRain -> 65
            is WeatherType.HeavyFreezingRain -> 67
            is WeatherType.SlightSnowFall -> 71
            is WeatherType.ModerateSnowFall -> 73
            is WeatherType.HeavySnowFall -> 75
            is WeatherType.SnowGrains -> 77
            is WeatherType.SlightRainShowers -> 80
            is WeatherType.ModerateRainShowers -> 81
            is WeatherType.ViolentRainShowers -> 82
            is WeatherType.SlightSnowShowers -> 85
            is WeatherType.HeavySnowShowers -> 86
            is WeatherType.ModerateThunderstorm -> 95
            is WeatherType.SlightHailThunderstorm -> 96
            is WeatherType.HeavyHailThunderstorm -> 99
        }

    @FromJson
    fun fromJson(code: Int): WeatherType = WeatherType.fromWMO(code)
}
