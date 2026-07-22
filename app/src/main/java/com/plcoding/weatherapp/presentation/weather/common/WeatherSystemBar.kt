package com.plcoding.weatherapp.presentation.weather.common

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

@Composable
fun WeatherSystemBar(
    backgroundColor: Color,
    useDarkIcons: Boolean,
) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? ComponentActivity

            activity?.enableEdgeToEdge(
                statusBarStyle =
                    if (useDarkIcons) {
                        SystemBarStyle.light(
                            backgroundColor.toArgb(),
                            backgroundColor.toArgb(),
                        )
                    } else {
                        SystemBarStyle.dark(
                            backgroundColor.toArgb(),
                        )
                    },
                navigationBarStyle =
                    if (useDarkIcons) {
                        SystemBarStyle.light(
                            backgroundColor.toArgb(),
                            backgroundColor.toArgb(),
                        )
                    } else {
                        SystemBarStyle.dark(
                            backgroundColor.toArgb(),
                        )
                    },
            )
        }
    }
}
