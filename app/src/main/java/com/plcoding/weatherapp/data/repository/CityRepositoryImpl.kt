package com.plcoding.weatherapp.data.repository

import android.util.Log
import com.plcoding.weatherapp.data.mappers.toCityDomain
import com.plcoding.weatherapp.data.remote.GeoCodingApi
import com.plcoding.weatherapp.data.remote.dtos.CityDto
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.repository.CityRepository
import com.plcoding.weatherapp.domain.util.DataError
import com.plcoding.weatherapp.domain.util.Result
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class CityRepositoryImpl
    @Inject
    constructor(
        private val geocodingApi: GeoCodingApi,
    ) : CityRepository {
        override suspend fun searchCities(query: String): Result<List<City>, DataError> {
            if (query.isBlank()) {
                return Result.Error(error = DataError.InvalidQuery)
            }
            return try {
                val response = geocodingApi.searchCities(query)
                val cities =
                    response
                        .results
                        .orEmpty()
                        .map(CityDto::toCityDomain)
                Result.Success(cities)
            } catch (_: IOException) {
                Result.Error(error = DataError.NoInternet)
            } catch (exception: HttpException) {
                Log.e(
                    "CitySearch",
                    """
                    HTTP error while searching for: $query
                    code=${exception.code()}
                    message=${exception.message()}
                    errorBody=${exception.response()?.errorBody()?.string()}
                    """.trimIndent(),
                    exception,
                )
                Result.Error(error = DataError.ServerError)
            } catch (exception: HttpException) {
                Result.Error(error = DataError.Unknown(exception))
            }
        }
    }
