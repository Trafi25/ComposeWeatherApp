package com.plcoding.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plcoding.weatherapp.data.local.CachedWeatherEntity

@Dao
internal interface CachedWeatherDao {
    @Query(
        """
    SELECT * FROM cached_weather WHERE locationKey = :locationKey LIMIT 1
    """,
    )
    suspend fun getWeather(locationKey: String): CachedWeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeather(weather: CachedWeatherEntity)

    @Query(
        """
        DELETE FROM cached_weather WHERE cachedAt < :oldestAllowedTimestamp
    """,
    )
    suspend fun deleteOlderThan(oldestAllowedTimestamp: Long)

    @Query("DELETE FROM cached_weather")
    suspend fun clearAll()
}
