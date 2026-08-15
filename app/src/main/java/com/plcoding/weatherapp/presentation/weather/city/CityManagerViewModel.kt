package com.plcoding.weatherapp.presentation.weather.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.repository.SelectedLocationRepository
import com.plcoding.weatherapp.domain.usecase.SavedCityUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityManagerViewModel
    @Inject
    constructor(
        private val cityUseCases: SavedCityUseCases,
        private val selectedLocationRepository: SelectedLocationRepository,
    ) : ViewModel() {
        private val _savedCities = MutableStateFlow<List<City>>(emptyList())
        val savedCities = _savedCities.asStateFlow()

        private val _selectedCityId = MutableStateFlow<Int?>(null)
        val selectedCityId = _selectedCityId.asStateFlow()

        init {
            viewModelScope.launch {
                cityUseCases.observeSavedCities().collect { cities ->
                    _savedCities.update { cities }
                }
            }
            viewModelScope.launch {
                selectedLocationRepository.observeSelectedCityId().collect { id ->
                    _selectedCityId.update { id }
                }
            }
        }

        fun onAction(action: CityManagerAction) {
            when (action) {
                is CityManagerAction.CitySelected -> selectCity(action.city)
                is CityManagerAction.CityDeleted -> deleteCity(action.cityId)
                CityManagerAction.CurrentLocationSelected -> selectCurrentLocation()
                CityManagerAction.AddCityClicked -> { /* Navigation handled in UI */ }
                CityManagerAction.BackClicked -> { /* Navigation handled in UI */ }
            }
        }

        private fun selectCity(city: City) {
            viewModelScope.launch {
                selectedLocationRepository.saveSelectedCityId(city.id)
            }
        }

        private fun selectCurrentLocation() {
            viewModelScope.launch {
                selectedLocationRepository.selectCurrentLocation()
            }
        }

        private fun deleteCity(cityId: Int) {
            viewModelScope.launch {
                cityUseCases.deleteCity(cityId)
                if (_selectedCityId.value == cityId) {
                    selectCurrentLocation()
                }
            }
        }
    }
