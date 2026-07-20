package com.plcoding.weatherapp.data.remote

internal object WeatherQueryFields {
    const val CURRENT =
        "temperature_2m," +
            "relative_humidity_2m," +
            "apparent_temperature," +
            "precipitation," +
            "weather_code," +
            "cloud_cover," +
            "pressure_msl," +
            "surface_pressure," +
            "wind_speed_10m," +
            "wind_direction_10m," +
            "wind_gusts_10m," +
            "is_day"

    const val HOURLY =
        "temperature_2m," +
            "relative_humidity_2m," +
            "weather_code," +
            "pressure_msl," +
            "wind_speed_10m," +
            "is_day"

    const val DAILY =
        "weather_code," +
            "temperature_2m_max," +
            "temperature_2m_min," +
            "sunrise," +
            "sunset"
}
