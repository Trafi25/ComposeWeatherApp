package com.plcoding.weatherapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastLocationStorage
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        private companion object {
            val KEY_LATITUDE = doublePreferencesKey("last_known_lat")
            val KEY_LONGITUDE = doublePreferencesKey("last_known_long")
            val KEY_LOCATION_NAME = stringPreferencesKey("last_known_name")
        }

        suspend fun save(
            latitude: Double,
            longitude: Double,
            name: String? = null,
        ) {
            context.locationDataStore.edit { prefs ->
                prefs[KEY_LATITUDE] = latitude
                prefs[KEY_LONGITUDE] = longitude
                name?.let { prefs[KEY_LOCATION_NAME] = it }
            }
        }

        fun observeLocation(): Flow<Triple<Double, Double, String?>?> =
            context.locationDataStore.data.map { prefs ->
                val lat = prefs[KEY_LATITUDE]
                val lon = prefs[KEY_LONGITUDE]
                val name = prefs[KEY_LOCATION_NAME]
                if (lat != null && lon != null) Triple(lat, lon, name) else null
            }
    }
