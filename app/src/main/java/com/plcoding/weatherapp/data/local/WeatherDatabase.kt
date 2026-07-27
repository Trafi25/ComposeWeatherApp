package com.plcoding.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.plcoding.weatherapp.data.local.dao.CachedWeatherDao
import com.plcoding.weatherapp.data.local.dao.SavedCityDao

@Database(entities = [SavedCityEntity::class, CachedWeatherEntity::class], version = 2, exportSchema = true)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun savedCityDao(): SavedCityDao

    abstract fun cachedWeatherDao(): CachedWeatherDao
}

val migration1To2 =
    object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS cached_weather (
                    locationKey TEXT NOT NULL PRIMARY KEY,
                    latitude REAL NOT NULL,
                    longitude REAL NOT NULL,
                    weatherJson TEXT NOT NULL,
                    cachedAt INTEGER NOT NULL
                )
                """.trimIndent(),
            )
        }
    }
