package com.plcoding.weatherapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastLocationStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private companion object {
        val KEY_LATITUDE = doublePreferencesKey("last_known_lat")
        val KEY_LONGITUDE = doublePreferencesKey("last_known_long")
    }

    suspend fun save(latitude: Double, longitude: Double) {
        context.locationDataStore.edit { prefs ->
            prefs[KEY_LATITUDE] = latitude
            prefs[KEY_LONGITUDE] = longitude
        }
    }

    fun observeLocation(): Flow<Pair<Double, Double>?> {
        return context.locationDataStore.data.map { prefs ->
            val lat = prefs[KEY_LATITUDE]
            val lon = prefs[KEY_LONGITUDE]
            if (lat != null && lon != null) lat to lon else null
        }
    }
}
