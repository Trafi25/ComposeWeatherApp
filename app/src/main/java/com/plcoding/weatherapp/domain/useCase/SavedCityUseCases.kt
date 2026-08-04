package com.plcoding.weatherapp.domain.usecase

import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.repository.SavedCityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A bundle of Use Cases for saved cities to keep AppModule clean.
 */
data class SavedCityUseCases
    @Inject
    constructor(
        val observeSavedCities: ObserveSavedCitiesUseCase,
        val saveCity: SaveCityUseCase,
        val deleteCity: DeleteCityUseCase,
        val getCity: GetCityUseCase,
    )

class ObserveSavedCitiesUseCase
    @Inject
    constructor(
        private val repository: SavedCityRepository,
    ) {
        operator fun invoke(): Flow<List<City>> = repository.observeSavedCities()
    }

class SaveCityUseCase
    @Inject
    constructor(
        private val repository: SavedCityRepository,
    ) {
        suspend operator fun invoke(city: City) = repository.saveCity(city)
    }

class DeleteCityUseCase
    @Inject
    constructor(
        private val repository: SavedCityRepository,
    ) {
        suspend operator fun invoke(cityId: Int) = repository.deleteCity(cityId)
    }

class GetCityUseCase
    @Inject
    constructor(
        private val repository: SavedCityRepository,
    ) {
        suspend operator fun invoke(cityId: Int): City? = repository.getCity(cityId)
    }
