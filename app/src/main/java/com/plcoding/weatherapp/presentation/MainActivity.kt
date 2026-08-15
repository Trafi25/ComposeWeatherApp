package com.plcoding.weatherapp.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.plcoding.weatherapp.presentation.formatter.LocalWeatherValueFormatter
import com.plcoding.weatherapp.presentation.formatter.WeatherValueFormatter
import com.plcoding.weatherapp.presentation.ui.theme.WeatherAppTheme
import com.plcoding.weatherapp.presentation.weather.WeatherAction
import com.plcoding.weatherapp.presentation.weather.WeatherRoute
import com.plcoding.weatherapp.presentation.weather.WeatherViewModel
import com.plcoding.weatherapp.presentation.weather.settings.SettingsViewModel
import com.plcoding.weatherapp.presentation.weather.state.WeatherEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var weatherValueFormatter: WeatherValueFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val weatherViewModel: WeatherViewModel = hiltViewModel()
            val settingsViewModel: SettingsViewModel = hiltViewModel()

            val settingsState by settingsViewModel.settings.collectAsState()

            val locationPermissionLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                ) { permissions ->
                    if (permissions.values.all { it }) {
                        weatherViewModel.onAction(WeatherAction.LocationPermissionGranted)
                    } else {
                        weatherViewModel.onAction(WeatherAction.LocationPermissionDenied)
                    }
                }

            LaunchedEffect(Unit) {
                weatherViewModel.effect.collectLatest { effect: WeatherEffect ->
                    when (effect) {
                        is WeatherEffect.RequestLocationPermission ->
                            {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                    ),
                                )
                            }
                        is WeatherEffect.RequestNotificationPermission -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                // todo notification
                            }
                        }

                        else -> {}
                    }
                }
            }

            CompositionLocalProvider(
                LocalWeatherValueFormatter provides weatherValueFormatter,
            ) {
                WeatherAppTheme(settings = settingsState) {
                    WeatherRoute()
                }
            }
        }
    }
}
