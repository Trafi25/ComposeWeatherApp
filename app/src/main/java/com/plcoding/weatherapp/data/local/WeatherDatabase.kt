package com.plcoding.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.plcoding.weatherapp.data.local.dao.SavedCityDao

@Database(entities = [SavedCityEntity::class], version = 1, exportSchema = true)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun savedCityDao(): SavedCityDao
}
