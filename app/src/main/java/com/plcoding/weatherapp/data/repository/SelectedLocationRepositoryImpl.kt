package com.plcoding.weatherapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.plcoding.weatherapp.data.preferences.locationDataStore
import com.plcoding.weatherapp.domain.repository.SelectedLocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SelectedLocationRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : SelectedLocationRepository {
        override fun observeSelectedCityId(): Flow<Int?> =
            context.locationDataStore.data.map { preferences ->
                preferences[SELECTED_CITY_ID]
            }

        override suspend fun saveSelectedCityId(cityId: Int) {
            context.locationDataStore.edit { preferences ->
                preferences[SELECTED_CITY_ID] = cityId
            }
        }

        override suspend fun selectCurrentLocation() {
            context.locationDataStore.edit { preferences ->
                preferences.remove(SELECTED_CITY_ID)
            }
        }

        private companion object {
            val SELECTED_CITY_ID = intPreferencesKey("selected_city_id")
        }
    }
