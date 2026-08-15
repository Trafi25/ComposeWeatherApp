package com.plcoding.weatherapp.presentation.weather

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.weatherapp.presentation.weather.city.CityManagerAction
import com.plcoding.weatherapp.presentation.weather.city.CityManagerViewModel
import com.plcoding.weatherapp.presentation.weather.city.CitySearchAction
import com.plcoding.weatherapp.presentation.weather.city.CitySearchViewModel
import com.plcoding.weatherapp.presentation.weather.settings.SettingsAction
import com.plcoding.weatherapp.presentation.weather.settings.SettingsViewModel
import com.plcoding.weatherapp.presentation.weather.state.WeatherEffect

@Composable
fun WeatherRoute(
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    searchViewModel: CitySearchViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    managerViewModel: CityManagerViewModel = hiltViewModel(),
) {
    val weatherState by weatherViewModel.uiState.collectAsStateWithLifecycle()
    val searchState by searchViewModel.state.collectAsStateWithLifecycle()
    val settingsState by settingsViewModel.settings.collectAsStateWithLifecycle()
    val savedCities by managerViewModel.savedCities.collectAsStateWithLifecycle()
    val selectedCityId by managerViewModel.selectedCityId.collectAsStateWithLifecycle()

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            val isLocationGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            weatherViewModel.onAction(
                if (isLocationGranted) {
                    WeatherAction.LocationPermissionGranted
                } else {
                    WeatherAction.LocationPermissionDenied
                },
            )
        }

    LaunchedEffect(Unit) {
        weatherViewModel.effect.collect { effect ->
            when (effect) {
                WeatherEffect.RequestLocationPermission -> {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        ),
                    )
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(Unit) {
        settingsViewModel.effect.collect { effect ->
            if (effect is WeatherEffect.RequestNotificationPermission) {
                // Trigger notification permission
            }
        }
    }

    WeatherMainScreen(
        weatherState = weatherState,
        searchState = searchState,
        settingsState = settingsState,
        savedCities = savedCities,
        selectedCityId = selectedCityId,
        onWeatherAction = { action ->
            when (action) {
                WeatherAction.RequestLocationPermission -> {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        ),
                    )
                }
                else -> weatherViewModel.onAction(action)
            }
        },
        onSearchAction = { action ->
            when (action) {
                CitySearchAction.BackClicked -> weatherViewModel.onAction(WeatherAction.BackClicked)
                else -> searchViewModel.onAction(action)
            }
        },
        onSettingsAction = { action ->
            when (action) {
                SettingsAction.BackClicked -> weatherViewModel.onAction(WeatherAction.BackClicked)
                else -> settingsViewModel.onAction(action)
            }
        },
        onManagerAction = { action ->
            when (action) {
                CityManagerAction.BackClicked -> weatherViewModel.onAction(WeatherAction.BackClicked)
                CityManagerAction.AddCityClicked -> weatherViewModel.onAction(WeatherAction.SearchCityClicked)
                else -> managerViewModel.onAction(action)
            }
        },
    )
}
