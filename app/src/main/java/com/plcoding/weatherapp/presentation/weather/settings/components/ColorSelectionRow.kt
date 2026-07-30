package com.plcoding.weatherapp.presentation.weather.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.domain.settings.AppAccentColor

@Composable
fun ColorSelectionRow(
    selectedColor: AppAccentColor,
    onColorSelected: (AppAccentColor) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AppAccentColor.entries.forEach { accent ->
            val color =
                when (accent) {
                    AppAccentColor.Green -> Color.Green
                    AppAccentColor.Blue -> Color.Blue
                    AppAccentColor.Red -> Color.Red
                }
            Box(
                modifier =
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = 3.dp,
                            color = if (selectedColor == accent) Color.White else Color.Transparent,
                            shape = CircleShape,
                        ).clickable(onClick = { onColorSelected(accent) }),
            )
        }
    }
}
