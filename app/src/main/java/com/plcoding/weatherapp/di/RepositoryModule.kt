package com.plcoding.weatherapp.di

import com.plcoding.weatherapp.data.repository.CityRepositoryImpl
import com.plcoding.weatherapp.data.repository.SavedCityRepositoryImpl
import com.plcoding.weatherapp.data.repository.SelectedLocationRepositoryImpl
import com.plcoding.weatherapp.data.repository.WeatherCacheRepositoryImpl
import com.plcoding.weatherapp.data.repository.WeatherRepositoryImpl
import com.plcoding.weatherapp.domain.repository.CityRepository
import com.plcoding.weatherapp.domain.repository.SavedCityRepository
import com.plcoding.weatherapp.domain.repository.SelectedLocationRepository
import com.plcoding.weatherapp.domain.repository.WeatherCacheRepository
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindCityRepository(cityRepositoryImpl: CityRepositoryImpl): CityRepository

    @Binds
    @Singleton
    abstract fun bindSavedCityRepository(implementation: SavedCityRepositoryImpl): SavedCityRepository

    @Binds
    @Singleton
    abstract fun bindSelectedLocationRepository(implementation: SelectedLocationRepositoryImpl): SelectedLocationRepository

    @Binds
    @Singleton
    abstract fun bindWeatherCacheRepository(implementation: WeatherCacheRepositoryImpl): WeatherCacheRepository
}
