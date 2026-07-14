package com.plcoding.weatherapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.weatherapp.presentation.ui.theme.DarkGreen
import com.plcoding.weatherapp.presentation.ui.theme.DeepGreenBackground

@Composable
fun WeatherMainScreen (viewModel: WeatherViewModel){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .statusBarsPadding(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(DarkGreen),
        ) {
            WeatherCard(
                state = uiState,
                backgroundColor = DeepGreenBackground,
            )
            Spacer(modifier = Modifier.height(16.dp))
            WeatherForeCast(state = uiState)
        }
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
