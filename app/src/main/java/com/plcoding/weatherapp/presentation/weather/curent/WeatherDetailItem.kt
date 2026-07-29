package com.plcoding.weatherapp.presentation.weather.curent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherDetailItem(
    iconResource: Int,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    labelColor: Color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
    valueColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(32.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = labelColor,
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor,
            )
        }
    }
}
