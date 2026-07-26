package com.plcoding.weatherapp.presentation.weather.city

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.presentation.ui.theme.DayBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityManagerScreen(
    cities: List<City>,
    onCityClick: (City) -> Unit,
    selectedCityId: Int?,
    onAddCityClick: () -> Unit,
    onCurrentLocationClick: () -> Unit,
    onCityDelete: (City) -> Unit,
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
                        text = "My cities",
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCityClick,
                containerColor = Color.White.copy(alpha = 0.9f),
                contentColor = backgroundColor,
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_icon),
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
                    .padding(paddingValues),
        ) {
            CurrentLocationItem(
                isSelected = selectedCityId == null,
                onClick = onCurrentLocationClick,
            )
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

            if (cities.isEmpty()) {
                EmptyCitiesContent(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                ) {
                    items(
                        items = cities,
                        key = { city -> city.id },
                    ) { city ->
                        CityManagerItem(
                            city = city,
                            isSelected = city.id == selectedCityId,
                            onClick = { onCityClick(city) },
                            onDelete = { onCityDelete(city) },
                        )
                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    }
                }
            }
        }
    }
}
