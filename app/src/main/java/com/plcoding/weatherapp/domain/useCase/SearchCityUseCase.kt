package com.plcoding.weatherapp.domain.useCase

import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.repository.CityRepository
import com.plcoding.weatherapp.domain.util.DataError
import com.plcoding.weatherapp.domain.util.Result
import javax.inject.Inject

/**
 * Use Case for searching cities.
 *
 * This encapsulates the specific "Business Rule" of searching.
 * The ViewModel shouldn't care about query length or trimming strings; it should just say "search".
 */
class SearchCityUseCase
    @Inject
    constructor(
        private val cityRepository: CityRepository,
    ) {
        suspend operator fun invoke(query: String): Result<List<City>, DataError> {
            val trimmedQuery = query.trim()

            if (trimmedQuery.length < 2) {
                return Result.Error(DataError.InvalidQuery)
            }

            return cityRepository.searchCities(trimmedQuery)
        }
    }
