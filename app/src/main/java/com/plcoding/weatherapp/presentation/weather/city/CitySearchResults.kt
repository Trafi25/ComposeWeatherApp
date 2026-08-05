package com.plcoding.weatherapp.presentation.weather.city

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.domain.location.City
import com.plcoding.weatherapp.presentation.weather.state.CitySearchState

@Composable
fun CitySearchResults(
    state: CitySearchState,
    onCityClick: (City) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        state.query.length < 2 -> {
            SearchMessage(
                text = "Enter at least two characters.",
                modifier = modifier,
            )
        }
        state.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        state.errorMessage != null ->
            {
                SearchMessage(
                    text = state.errorMessage,
                    modifier = modifier,
                )
            }
        state.query.isBlank() ->
            {
                SearchMessage(
                    text = "Enter a city name to begin searching.",
                    modifier = modifier,
                )
            }
        state.results.isEmpty() ->
            SearchMessage(
                text = "No cities found.",
                modifier = modifier,
            )
        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
            ) {
                items(
                    items = state.results,
                    key = { city -> "${city.id}" },
                ) { city ->
                    CitySearchResultItem(
                        city = city,
                        onClick = { onCityClick(city) },
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color.White.copy(alpha = 0.2f),
                    )
                }
            }
        }
    }
}

@Composable
fun SearchMessage(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(24.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.White.copy(alpha = 0.8f),
        )
    }
}
