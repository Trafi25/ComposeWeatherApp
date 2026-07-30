package com.plcoding.weatherapp.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Custom Spacing Design Tokens.
 * Instead of hardcoding 16.dp, use MaterialTheme.spacing.medium
 */
data class Spacing(
    val default: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val giant: Dp = 48.dp,
)

/**
 * Custom Component Size Design Tokens.
 * For fixed dimensions like icons and card heights.
 */
data class Sizes(
    val iconSmall: Dp = 16.dp,
    val iconMedium: Dp = 24.dp,
    val iconLarge: Dp = 32.dp,
    val weatherIconSmall: Dp = 35.dp,
    val weatherIconLarge: Dp = 150.dp,
    val hourlyCardHeight: Dp = 100.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
val LocalSizes = staticCompositionLocalOf { Sizes() }

/**
 * Extension properties to make accessing tokens easy:
 * MaterialTheme.spacing and MaterialTheme.sizes
 */
val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current

val MaterialTheme.sizes: Sizes
    @Composable
    @ReadOnlyComposable
    get() = LocalSizes.current
