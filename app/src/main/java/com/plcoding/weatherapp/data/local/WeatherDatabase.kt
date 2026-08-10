package com.plcoding.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.plcoding.weatherapp.data.local.dao.CachedAiSummaryDao
import com.plcoding.weatherapp.data.local.dao.CachedWeatherDao
import com.plcoding.weatherapp.data.local.dao.SavedCityDao

@Database(
    entities =
        [
            SavedCityEntity::class,
            CachedWeatherEntity::class,
            CachedAiSummaryEntity::class,
        ],
    version = 3,
    exportSchema = true,
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun savedCityDao(): SavedCityDao

    abstract fun cachedWeatherDao(): CachedWeatherDao

    abstract fun cachedWeatherAi(): CachedAiSummaryDao
}

val migration2To3 =
    object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS ai_weather_summary (
                    locationKey TEXT NOT NULL PRIMARY KEY,
                    summary TEXT NOT NULL,
                    createdAt INTEGER NOT NULL
                )
                """.trimIndent(),
            )
        }
    }
