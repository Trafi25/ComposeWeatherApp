package com.plcoding.weatherapp.presentation.weather.city

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.plcoding.weatherapp.presentation.weather.state.CitySearchState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchScreen(
    state: CitySearchState,
    onAction: (CitySearchAction) -> Unit,
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
                        text = "Search city",
                        fontWeight = FontWeight.SemiBold,
                        color = onSurfaceColor,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(CitySearchAction.BackClicked) }) {
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
                    .padding(horizontal = 16.dp),
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { onAction(CitySearchAction.QueryChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Enter city name", color = onSurfaceColor.copy(alpha = 0.7f))
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = null,
                        tint = onSurfaceColor,
                        modifier = Modifier.size(20.dp),
                    )
                },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(onClick = { onAction(CitySearchAction.ClearQuery) }) {
                            Icon(
                                painter = painterResource(R.drawable.close),
                                contentDescription = "Clear",
                                tint = onSurfaceColor,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        imeAction = androidx.compose.ui.text.input.ImeAction.Search,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onSearch = {
                            // Search is already debounced in ViewModel
                        },
                    ),
                shape = RoundedCornerShape(12.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = onSurfaceColor.copy(alpha = 0.1f),
                    ),
            )
            Spacer(modifier = Modifier.height(16.dp))

            CitySearchResults(
                state = state,
                onCityClick = { onAction(CitySearchAction.CitySelected(it)) },
            )
        }
    }
}
