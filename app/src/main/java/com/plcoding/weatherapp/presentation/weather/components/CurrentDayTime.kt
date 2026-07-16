package com.plcoding.weatherapp.presentation.weather.components


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CurrentDayTime(
    modifier: Modifier = Modifier
) {

    var currentDateTime by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true){
            currentDateTime = LocalDateTime.now()
            delay(60_000L)
        }
    }

    val formatter =
        DateTimeFormatter.ofPattern(
            "EEE, dd MMM • HH:mm",
            LocalLocale.current.platformLocale,
        )
    Text(
        text = "Today ${
            currentDateTime.format(
                formatter,
            )
        }",
        modifier = modifier,
        color = Color.White,
    )
}
