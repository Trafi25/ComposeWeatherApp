package com.plcoding.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_weather_summary")
data class CachedAiSummaryEntity(
    @PrimaryKey
    val locationKey: String,
    val summary: String,
    val createdAt: Long,
)
