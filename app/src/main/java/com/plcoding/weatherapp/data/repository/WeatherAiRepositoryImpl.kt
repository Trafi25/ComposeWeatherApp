package com.plcoding.weatherapp.data.repository

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.plcoding.weatherapp.data.local.CachedAiSummaryEntity
import com.plcoding.weatherapp.data.local.dao.CachedAiSummaryDao
import com.plcoding.weatherapp.data.local.weatherLocationKey
import com.plcoding.weatherapp.domain.repository.WeatherAiRepository
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

internal class WeatherAiRepositoryImpl
    @Inject
    constructor(
        private val aiSummaryDao: CachedAiSummaryDao,
    ) : WeatherAiRepository {
        private val model =
            Firebase
                .ai(backend = GenerativeBackend.googleAI())
                .generativeModel(modelName = "gemini-3.5-flash-lite")

        override suspend fun generateWeatherSummary(
            weatherInfo: WeatherInfo,
            locationName: String,
            latitude: Double,
            longitude: Double,
        ): String {
            val locationKey = weatherLocationKey(latitude, longitude)
            val cachedSummary = aiSummaryDao.getSummary(locationKey)

            if (cachedSummary != null) {
                val cacheAge = System.currentTimeMillis() - cachedSummary.createdAt

                if (cacheAge < CACHE_DURATION) {
                    return cachedSummary.summary
                }
            }

            val current = weatherInfo.currentWeatherData ?: return "WeatherInformation unavailable"
            val prompt =
                """
                You are a weather assistant.

                Create a short practical weather recommendation.

                Location: $locationName
                Temperature: ${current.temperatureCelsius}°C
                Feels like: ${current.apparentTemperatureCelsius}°C
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
            val summary = response.text ?: return "No weather recommendation available."

            if (summary.isNotBlank() && summary != "No weather recommendation available.") {
                aiSummaryDao.saveSummary(
                    CachedAiSummaryEntity(
                        locationKey = locationKey,
                        summary = summary,
                        createdAt = System.currentTimeMillis(),
                    ),
                )
            }

            return summary
        }

        companion object {
            private const val CACHE_DURATION =
                2 * 60 * 60 * 1000L
        }
    }
