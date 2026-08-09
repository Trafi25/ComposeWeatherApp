package com.plcoding.weatherapp.data.repository

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.plcoding.weatherapp.domain.repository.WeatherAiRepository
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherAiRepositoryImpl @Inject constructor(

): WeatherAiRepository {
    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(modelName = "gemini-3.6-flash")

    override suspend fun generateWeatherSummary(
        weatherInfo: WeatherInfo,
        locationName: String,
    ): String {
        val current = weatherInfo.currentWeatherData ?: return "WeatherInformation unavailable"
        val prompt = """
            You are a weather assistant.

            Create a short practical weather recommendation.

            Location: $locationName
            Temperature: ${current.temperatureCelsius}°C
            Feels like: ${current.temperatureCelsius}°C
            Humidity: ${current.humidity}%
            Wind speed: ${current.windSpeed} km/h
            Precipitation: ${current.precipitationMm} mm

            Give at most 2 short sentences.

            Mention useful advice when relevant, for example:
            - what should person wear to go outside
            - take an umbrella
            - avoid long outdoor activity because of heat
            - strong wind warning

            Do not repeat all weather values.
        """.trimIndent()

        val response = model.generateContent(prompt)

        return response.text ?: "No weather recommendation available."

    }
}
