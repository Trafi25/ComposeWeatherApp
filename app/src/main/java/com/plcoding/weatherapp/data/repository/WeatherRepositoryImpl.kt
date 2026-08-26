package com.plcoding.weatherapp.data.repository

import com.plcoding.weatherapp.data.local.dao.CachedWeatherDao
import com.plcoding.weatherapp.data.local.mapper.CachedWeatherMapper
import com.plcoding.weatherapp.data.local.weatherLocationKey
import com.plcoding.weatherapp.data.mappers.toWeatherInfo
import com.plcoding.weatherapp.data.remote.WeatherApi
import com.plcoding.weatherapp.data.util.toWeatherError
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.DataError
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class WeatherRepositoryImpl
    @Inject
    constructor(
        private val api: WeatherApi,
        private val cachedWeatherDao: CachedWeatherDao,
        private val cachedWeatherMapper: CachedWeatherMapper,
    ) : WeatherRepository {
        override suspend fun getWeatherData(
            lat: Double,
            long: Double,
        ): Result<WeatherInfo, DataError> {
            val locationKey =
                weatherLocationKey(
                    latitude = lat,
                    longitude = long,
                )
            return try {
                val weatherInfo =
                    api
                        .getWeatherData(
                            lat = lat,
                            long = long,
                        ).toWeatherInfo()

                val cachedWeather =
                    cachedWeatherMapper.toEntity(
                        weatherInfo = weatherInfo,
                        locationKey = locationKey,
                        latitude = lat,
                        longitude = long,
                    )
                cachedWeatherDao.saveWeather(
                    weather = cachedWeather,
                )
                Result.Success(weatherInfo)
            } catch (exception: Exception) {
                when (exception) {
                    is IOException, is HttpException -> loadCachedWeather(locationKey)
                    else -> {
                        Result.Error(
                            error = exception.toWeatherError(),
                        )
                    }
                }
            }
        }

        private suspend fun loadCachedWeather(locationKey: String): Result<WeatherInfo, DataError> {
            val cachedEntity =
                cachedWeatherDao.getWeather(locationKey) ?: return Result.Error(DataError.NoInternet)
            val cachedWeather = cachedWeatherMapper.toDomain(cachedEntity)
            return if (cachedWeather != null) Result.Success(cachedWeather) else Result.Error(DataError.NoInternet)
        }
    }
