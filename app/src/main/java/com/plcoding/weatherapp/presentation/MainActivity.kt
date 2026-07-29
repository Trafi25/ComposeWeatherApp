package com.plcoding.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plcoding.weatherapp.presentation.formatter.LocalWeatherValueFormatter
import com.plcoding.weatherapp.presentation.formatter.WeatherValueFormatter
import com.plcoding.weatherapp.presentation.ui.theme.WeatherAppTheme
import com.plcoding.weatherapp.presentation.weather.WeatherRoute
import com.plcoding.weatherapp.presentation.weather.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var weatherValueFormatter: WeatherValueFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: WeatherViewModel = viewModel()
            val state by viewModel.uiState.collectAsState()

            CompositionLocalProvider(
                LocalWeatherValueFormatter provides weatherValueFormatter,
            ) {
                WeatherAppTheme(settings = state.appSettings) {
                    WeatherRoute()
                }
            }
        }
    }
}
