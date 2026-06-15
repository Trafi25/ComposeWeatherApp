package com.plcoding.weatherapp.di

import com.plcoding.weatherapp.data.location.DefaultLocationTracker
import com.plcoding.weatherapp.domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
@Singleton
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun binLocationTracker(defaultLocationTracker : DefaultLocationTracker) : LocationTracker

}