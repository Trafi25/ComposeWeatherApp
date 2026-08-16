package com.plcoding.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plcoding.weatherapp.data.local.SavedCityEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SavedCityDao {
    @Query(
        """
            SELECT * FROM saved_cities ORDER BY name ASC
        """,
    )
    fun observeSavedCities(): Flow<List<SavedCityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: SavedCityEntity)

    @Query(
        """
            DELETE FROM saved_cities WHERE id =:cityId
        """,
    )
    suspend fun deleteCityById(cityId: Int)

    @Query(
        """
        SELECT * FROM saved_cities  WHERE id =:cityId LIMIT 1
    """,
    )
    suspend fun getCityById(cityId: Int): SavedCityEntity?
}
