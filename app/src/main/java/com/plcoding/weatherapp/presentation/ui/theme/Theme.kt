package com.plcoding.weatherapp.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.plcoding.weatherapp.domain.settings.AppSettings

@Composable
fun WeatherAppTheme(
    settings: AppSettings,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        getThemeColorScheme(
            themeMode = settings.themeMode,
            accentColor = settings.accentColor,
            isSystemDark = isSystemInDarkTheme(),
        )

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalSizes provides Sizes(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content,
        )
    }
}
