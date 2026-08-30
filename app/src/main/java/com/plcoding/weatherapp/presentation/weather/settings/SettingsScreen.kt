package com.plcoding.weatherapp.presentation.weather.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.domain.settings.displayName
import com.plcoding.weatherapp.presentation.TestTags
import com.plcoding.weatherapp.presentation.weather.settings.components.ColorSelectionRow
import com.plcoding.weatherapp.presentation.weather.settings.components.SettingsItem
import com.plcoding.weatherapp.presentation.weather.settings.components.SettingsSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        color = onSurfaceColor,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(SettingsAction.BackClicked) },
                        modifier = Modifier.testTag(TestTags.SETTINGS_BACK_BUTTON),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_icon),
                            contentDescription = "Back",
                            tint = onSurfaceColor,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                modifier = Modifier.statusBarsPadding(),
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .testTag(TestTags.SETTINGS_CONTENT),
        ) {
            SettingsSection(title = "UNITS") {
                SettingsItem(
                    title = "Temperature",
                    description = settings.temperatureUnit.displayName(),
                    onClick = { onAction(SettingsAction.ToggleTemperatureUnit) },
                )
                SettingsItem(
                    title = "Wind speed",
                    description = settings.windSpeedUnit.displayName(),
                    onClick = { onAction(SettingsAction.ToggleWindSpeedUnit) },
                )
                SettingsItem(
                    title = "Pressure",
                    description = settings.pressureUnit.displayName(),
                    onClick = { onAction(SettingsAction.TogglePressureUnit) },
                )
                SettingsItem(
                    title = "Precipitation",
                    description = settings.precipitationUnit.displayName(),
                    onClick = { onAction(SettingsAction.TogglePrecipitationUnit) },
                )
                SettingsItem(
                    title = "Time format",
                    description = settings.timeFormat.displayName(),
                    onClick = { onAction(SettingsAction.ToggleTimeFormat) },
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = onSurfaceColor.copy(alpha = 0.2f),
            )

            SettingsSection(title = "APPEARANCE") {
                SettingsItem(
                    title = "Theme",
                    description = settings.themeMode.displayName(),
                    onClick = { onAction(SettingsAction.ToggleThemeMode) },
                )

                Text(
                    text = "Accent Color",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                ColorSelectionRow(
                    selectedColor = settings.accentColor,
                    onColorSelected = { onAction(SettingsAction.AccentColorSelected(it)) },
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = onSurfaceColor.copy(alpha = 0.2f),
            )

            SettingsSection(title = "DATA") {
                SettingsItem(
                    title = "Weather Notifications",
                    description = if (settings.weatherNotificationEnabled) "Enabled" else "Disabled",
                    onClick = { onAction(SettingsAction.ToggleNotifications) },
                    trailingContent = null,
                )

                SettingsItem(
                    title = "Clear weather cache",
                    description = "Remove all offline data",
                    onClick = { onAction(SettingsAction.ClearCache) },
                    trailingContent = null,
                )
            }
        }
    }
}
