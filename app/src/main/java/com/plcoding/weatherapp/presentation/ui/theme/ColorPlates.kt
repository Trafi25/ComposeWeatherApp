package com.plcoding.weatherapp.presentation.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.plcoding.weatherapp.domain.settings.AppAccentColor
import com.plcoding.weatherapp.domain.settings.AppThemeMode

fun getThemeColorScheme(
    themeMode: AppThemeMode,
    accentColor: AppAccentColor,
    isSystemDark: Boolean,
): ColorScheme {
    val useDarkMode =
        when (themeMode) {
            AppThemeMode.Light -> false
            AppThemeMode.Dark -> true
            AppThemeMode.SystemDefault -> isSystemDark
        }

    return when (accentColor) {
        AppAccentColor.Green -> if (useDarkMode) DarkGreenScheme else LightGreenScheme
        AppAccentColor.Blue -> if (useDarkMode) DarkBlueScheme else LightBlueScheme
        AppAccentColor.Red -> if (useDarkMode) DarkRedScheme else LightRedScheme
    }
}

private val DarkGreenScheme =
    darkColorScheme(
        primary = Color(0xFF66B5A8),
        onPrimary = Color(0xFF082F2A),
        secondary = Color(0xFF8FD2C7),
        onSecondary = Color(0xFF082F2A),
        background = Color(0xFF082F2A),
        onBackground = Color.White,
        surface = Color(0xFF124B43),
        onSurface = Color.White,
    )

private val DarkBlueScheme =
    darkColorScheme(
        primary = Color(0xFF669BB5),
        onPrimary = Color(0xFF08212F),
        secondary = Color(0xFF8FC0D8),
        onSecondary = Color(0xFF08212F),
        background = Color(0xFF08212F),
        onBackground = Color.White,
        surface = Color(0xFF12394B),
        onSurface = Color.White,
    )

private val DarkRedScheme =
    darkColorScheme(
        primary = Color(0xFFB57474),
        onPrimary = Color(0xFF2F1010),
        secondary = Color(0xFFD99A9A),
        onSecondary = Color(0xFF2F1010),
        background = Color(0xFF2F1010),
        onBackground = Color.White,
        surface = Color(0xFF4B2020),
        onSurface = Color.White,
    )

private val LightGreenScheme =
    lightColorScheme(
        primary = Color(0xFF124B43),
        onPrimary = Color.White,
        secondary = Color(0xFF8FD2C7),
        onSecondary = Color(0xFF082F2A),
        background = Color(0xFF4A9D8F),
        onBackground = Color.White,
        surface = Color(0xFF66B5A8),
        onSurface = Color.White,
    )

private val LightBlueScheme =
    lightColorScheme(
        primary = Color(0xFF12394B),
        onPrimary = Color.White,
        secondary = Color(0xFF8FC0D8),
        onSecondary = Color(0xFF08212F),
        background = Color(0xFF4A7F9D),
        onBackground = Color.White,
        surface = Color(0xFF669BB5),
        onSurface = Color.White,
    )

private val LightRedScheme =
    lightColorScheme(
        primary = Color(0xFF4B2020),
        onPrimary = Color.White,
        secondary = Color(0xFFD99A9A),
        onSecondary = Color(0xFF2F1010),
        background = Color(0xFF9D5A5A),
        onBackground = Color.White,
        surface = Color(0xFFB57474),
        onSurface = Color.White,
    )
