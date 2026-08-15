package com.plcoding.weatherapp.presentation.weather.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.domain.repository.SelectedLocationRepository
import com.plcoding.weatherapp.domain.usecase.SavedCityUseCases
import com.plcoding.weatherapp.domain.usecase.SearchCityUseCase
import com.plcoding.weatherapp.domain.util.Result
import com.plcoding.weatherapp.domain.util.toMessage
import com.plcoding.weatherapp.presentation.weather.state.CitySearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitySearchViewModel
    @Inject
    constructor(
        private val searchCityUseCase: SearchCityUseCase,
        private val cityUseCases: SavedCityUseCases,
        private val selectedLocationRepository: SelectedLocationRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow(CitySearchState())
        val state = _state.asStateFlow()

        private var searchJob: Job? = null

        fun onAction(action: CitySearchAction) {
            when (action) {
                is CitySearchAction.QueryChanged -> onQueryChanged(action.query)
                is CitySearchAction.CitySelected -> onCitySelected(action.city)
                CitySearchAction.ClearQuery -> onQueryChanged("")
                CitySearchAction.BackClicked -> { /* Handled by WeatherViewModel */ }
            }
        }

        private fun onQueryChanged(query: String) {
            _state.update { it.copy(query = query, errorMessage = null) }
            searchJob?.cancel()

            if (query.length < 2) {
                _state.update { it.copy(results = emptyList(), isLoading = false) }
                return
            }

            searchJob =
                viewModelScope.launch {
                    delay(500L)
                    _state.update { it.copy(isLoading = true) }
                    when (val result = searchCityUseCase(query)) {
                        is Result.Success -> {
                            if (_state.value.query == query) {
                                _state.update { it.copy(results = result.data, isLoading = false) }
                            }
                        }
                        is Result.Error -> {
                            if (_state.value.query == query) {
                                _state.update { it.copy(results = emptyList(), isLoading = false, errorMessage = result.error.toMessage()) }
                            }
                        }
                    }
                }
        }

        private fun onCitySelected(city: City) {
            viewModelScope.launch {
                cityUseCases.saveCity(city)
                selectedLocationRepository.saveSelectedCityId(city.id)
                _state.update { CitySearchState() } // Reset search
            }
        }
    }
