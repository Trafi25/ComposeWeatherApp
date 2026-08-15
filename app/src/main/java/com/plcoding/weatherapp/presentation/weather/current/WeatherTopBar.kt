package com.plcoding.weatherapp.presentation.weather.current

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.R

@Composable
fun WeatherTopBar(
    locationName: String?,
    onSearchClick: () -> Unit,
    onManageCitiesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 4.dp,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = locationName ?: "Current location",
            modifier =
                Modifier
                    .weight(1f)
                    .basicMarquee(),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            maxLines = 1,
        )

        IconButton(
            onClick = onSearchClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search city",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }

        IconButton(
            onClick = onManageCitiesClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.management),
                contentDescription = "Manage cities",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }

        IconButton(onClick = onSettingsClick) {
            Icon(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
