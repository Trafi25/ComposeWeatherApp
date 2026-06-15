package com.plcoding.weatherapp.di

import com.plcoding.weatherapp.data.location.DefaultLocationTracker
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
@Singleton
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun binWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl) : WeatherRepository

}