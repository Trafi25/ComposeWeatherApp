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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.domain.settings.AppSettings
import com.plcoding.weatherapp.domain.settings.displayName
import com.plcoding.weatherapp.presentation.ui.theme.DayBackground
import com.plcoding.weatherapp.presentation.weather.settings.components.SettingsItem
import com.plcoding.weatherapp.presentation.weather.settings.components.SettingsSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    onTemperatureUnitClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = DayBackground,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.back_icon),
                            contentDescription = "Back",
                            tint = Color.White,
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            SettingsSection(title = "UNITS") {
                SettingsItem(
                    title = "Temperature",
                    description = settings.temperatureUnit.displayName(),
                    onClick = onTemperatureUnitClick
                )
                SettingsItem(
                    title = "Wind speed",
                    description = settings.windSpeedUnit.displayName(),
                    onClick = {}
                )
                SettingsItem(
                    title = "Pressure",
                    description = settings.pressureUnit.displayName(),
                    onClick = {}
                )
                SettingsItem(
                    title = "Precipitation",
                    description = settings.precipitationUnit.displayName(),
                    onClick = {}
                )
                SettingsItem(
                    title = "Time format",
                    description = settings.timeFormat.displayName(),
                    onClick = {}
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.White.copy(alpha = 0.2f)
            )

            SettingsSection(title = "APPEARANCE") {
                SettingsItem(
                    title = "Accent Color",
                    description = settings.accentColor.displayName(),
                    onClick = {}
                )
                SettingsItem(
                    title = "Theme",
                    description = settings.themeMode.displayName(),
                    onClick = {}
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.White.copy(alpha = 0.2f)
            )

            SettingsSection(title = "DATA") {
                SettingsItem(
                    title = "Clear weather cache",
                    description = "Remove all offline data",
                    onClick = {},
                    trailingContent = null
                )
            }
        }
    }
}
