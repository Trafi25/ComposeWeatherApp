package com.plcoding.weatherapp.di

import com.plcoding.weatherapp.data.repository.SettingsRepositoryImpl
import com.plcoding.weatherapp.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(implementation: SettingsRepositoryImpl): SettingsRepository
}
