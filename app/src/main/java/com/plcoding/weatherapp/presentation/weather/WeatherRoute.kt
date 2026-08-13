package com.plcoding.weatherapp.presentation.weather

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.weatherapp.presentation.weather.state.WeatherEffect

@Composable
fun WeatherRoute(viewModel: WeatherViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            val isLocationGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            viewModel.onAction(
                if (isLocationGranted) {
                    WeatherAction.LocationPermissionGranted
                } else {
                    WeatherAction.LocationPermissionDenied
                },
            )
        }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                WeatherEffect.RequestLocationPermission -> {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        ),
                    )
                }

                WeatherEffect.RequestNotificationPermission -> {
                    // Handle notification permission if needed for Android 13+
                }

                is WeatherEffect.ShowSnackbar -> {
                    // Handle snackbar
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted || coarseLocationGranted) {
            viewModel.onAction(WeatherAction.LocationPermissionGranted)
        } else {
            viewModel.onAction(WeatherAction.RequestLocationPermission)
        }
    }

    WeatherMainScreen(
        uiState = state,
        onAction = viewModel::onAction,
    )
}
