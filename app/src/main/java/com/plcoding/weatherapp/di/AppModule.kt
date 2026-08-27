package com.plcoding.weatherapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.plcoding.weatherapp.data.local.WeatherDatabase
import com.plcoding.weatherapp.data.local.dao.CachedAiSummaryDao
import com.plcoding.weatherapp.data.local.dao.CachedWeatherDao
import com.plcoding.weatherapp.data.local.dao.SavedCityDao
import com.plcoding.weatherapp.data.local.migration2To3
import com.plcoding.weatherapp.data.local.util.LocalDateAdapter
import com.plcoding.weatherapp.data.local.util.LocalDateTimeAdapter
import com.plcoding.weatherapp.data.local.util.WeatherTypeAdapter
import com.plcoding.weatherapp.data.preferences.LastLocationStorage
import com.plcoding.weatherapp.data.remote.GeoCodingApi
import com.plcoding.weatherapp.data.remote.WeatherApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    private const val WEATHER_BASE_URL =
        "https://api.open-meteo.com/"

    private const val GEOCODING_BASE_URL =
        "https://geocoding-api.open-meteo.com/"

    private const val WEATHER_DATABASE_NAME =
        "weather_database"

    @Provides
    @Singleton
    fun provideWeatherApi(moshi: Moshi): WeatherApi =
        Retrofit
            .Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideGeocodingApi(moshi: Moshi): GeoCodingApi =
        Retrofit
            .Builder()
            .baseUrl(GEOCODING_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeoCodingApi::class.java)

    @Provides
    @Singleton
    fun provideWeatherDatabase(app: Application): WeatherDatabase =
        Room
            .databaseBuilder(
                app,
                WeatherDatabase::class.java,
                WEATHER_DATABASE_NAME,
            ).addMigrations(migration2To3)
            .build()

    @Provides
    @Singleton
    fun providesSavedCityDao(database: WeatherDatabase): SavedCityDao = database.savedCityDao()

    @Provides
    @Singleton
    fun provideCachedWeatherDao(database: WeatherDatabase): CachedWeatherDao = database.cachedWeatherDao()

    @Provides
    @Singleton
    fun provideCachedAiSummary(database: WeatherDatabase): CachedAiSummaryDao = database.cachedWeatherAi()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi
            .Builder()
            .add(LocalDateTimeAdapter())
            .add(LocalDateAdapter())
            .add(WeatherTypeAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(app)

    @Provides
    @Singleton
    fun provideLastLocationStorage(@ApplicationContext context: Context): LastLocationStorage =
        LastLocationStorage(context)
}
