package com.plcoding.weatherapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private const val LOCATION_PREFERENCES = "location_preferences"

val Context.locationDataStore: DataStore<Preferences> by preferencesDataStore(
    name = LOCATION_PREFERENCES,
)

val Context.settingsDataStore by preferencesDataStore("app_settings")
