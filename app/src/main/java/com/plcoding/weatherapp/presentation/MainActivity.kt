package com.plcoding.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.plcoding.weatherapp.presentation.ui.theme.DeepGreenBackground
import com.plcoding.weatherapp.presentation.ui.theme.WeatherAppTheme
import com.plcoding.weatherapp.presentation.weather.WeatherRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(DeepGreenBackground.toArgb())
        )
        setContent {
            WeatherAppTheme {
                WeatherRoute()
            }
        }
    }
}
