package com.plcoding.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plcoding.weatherapp.data.local.CachedAiSummaryEntity

@Dao
interface CachedAiSummaryDao {

    @Query(
        """
    SELECT * FROM ai_weather_summary
    WHERE locationKey = :locationKey
    LIMIT 1
    """,
    )
    suspend fun getSummary(locationKey: String): CachedAiSummaryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSummary(entity: CachedAiSummaryEntity)

    @Query(
        """
    DELETE FROM ai_weather_summary
    WHERE createdAt < :timestamp
    """,
    )
    suspend fun deleteOlderThan(timestamp: Long)

    @Query("DELETE FROM ai_weather_summary")
    suspend fun clear()


}
