package com.plcoding.weatherapp.di

import android.app.Application
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.plcoding.weatherapp.data.local.WeatherDatabase
import com.plcoding.weatherapp.data.local.dao.SavedCityDao
import com.plcoding.weatherapp.data.remote.GeoCodingApi
import com.plcoding.weatherapp.data.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val WEATHER_BASE_URL =
        "https://api.open-meteo.com/"

    private const val GEOCODING_BASE_URL =
        "https://geocoding-api.open-meteo.com/"

    private const val WEATHER_DATABASE_NAME =
        "weather_database"

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi =
        Retrofit
            .Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideGeocodingApi(): GeoCodingApi =
        Retrofit
            .Builder()
            .baseUrl(GEOCODING_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
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
            ).build()

    @Provides
    @Singleton
    fun providesSavedCityDao(database: WeatherDatabase): SavedCityDao = database.savedCityDao()

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(app)
}
