package com.plcoding.weatherapp.presentation.weather.city

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.presentation.ui.theme.DayBackground
import com.plcoding.weatherapp.presentation.ui.theme.NightBackground
import com.plcoding.weatherapp.presentation.weather.states.WeatherState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCitiesScreen(
    uiState: WeatherState,
    onBackClick: () -> Unit,
    onAddCityClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDay = uiState.weatherInfo?.currentWeatherData?.isDay ?: true
    val backgroundColor = if (isDay) DayBackground else NightBackground

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = backgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Manage cities",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_icon),
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCityClick,
                containerColor = Color.White.copy(alpha = 0.9f),
                contentColor = backgroundColor,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_icon),
                    contentDescription = "Add city",
                    modifier = Modifier.size(24.dp),
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                readOnly = true,
                enabled = true,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { onAddCityClick() },
                placeholder = {
                    Text(text = "Search city", color = Color.White.copy(alpha = 0.7f))
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.White,
                        disabledBorderColor = Color.White.copy(alpha = 0.5f),
                        disabledLeadingIconColor = Color.White,
                        disabledPlaceholderColor = Color.White.copy(alpha = 0.7f),
                        disabledContainerColor = Color.White.copy(alpha = 0.1f),
                    ),
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Current Location / Added Cities will be here
        }
    }
}
