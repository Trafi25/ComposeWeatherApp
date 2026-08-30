package com.plcoding.weatherapp.presentation.weather.city

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.presentation.TestTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityManagerScreen(
    cities: List<City>,
    selectedCityId: Int?,
    onAction: (CityManagerAction) -> Unit,
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
                        text = "My cities",
                        color = onSurfaceColor,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(CityManagerAction.BackClicked) }) {
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(CityManagerAction.AddCityClicked) },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag(TestTags.CITY_MANAGER_ADD_BUTTON),
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_icon),
                    contentDescription = "Add city",
                    modifier = Modifier.size(24.dp),
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier =
                    Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxHeight(),
            ) {
                CurrentLocationItem(
                    isSelected = selectedCityId == null,
                    onClick = { onAction(CityManagerAction.CurrentLocationSelected) },
                )
                HorizontalDivider(color = onSurfaceColor.copy(alpha = 0.2f))

                if (cities.isEmpty()) {
                    EmptyCitiesContent(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f),
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f).testTag(TestTags.CITY_MANAGER_LIST),
                    ) {
                        items(
                            items = cities,
                            key = { city -> city.id },
                        ) { city ->
                            CityManagerItem(
                                city = city,
                                isSelected = city.id == selectedCityId,
                                onClick = { onAction(CityManagerAction.CitySelected(city)) },
                                onDelete = { onAction(CityManagerAction.CityDeleted(city.id)) },
                            )
                            HorizontalDivider(color = onSurfaceColor.copy(alpha = 0.2f))
                        }
                    }
                }
            }
        }
    }
}
