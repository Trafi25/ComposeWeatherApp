package com.plcoding.weatherapp.presentation.weather.daily

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.weatherapp.domain.weather.DailyWeatherData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun DailyWeatherItem(
    weather: DailyWeatherData,
    modifier: Modifier = Modifier,
) {
    val today = LocalDate.now()
    val locale = LocalLocale.current.platformLocale
    val dayFormatter =
        remember(locale) {
            DateTimeFormatter.ofPattern("EEE", locale)
        }
    val dateFormatter =
        remember(locale) {
            DateTimeFormatter.ofPattern("dd MMM", locale)
        }

    val dayText =
        when (weather.date) {
            today -> "Today"
            today.plusDays(1) -> "Tomorrow"
            else -> weather.date.format(dayFormatter)
        }
    val dateText = weather.date.format(dateFormatter)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = 72.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.width(76.dp),
        ) {
            Text(
                text = dayText,
                modifier = Modifier.width(88.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
            )
            Text(
                text = dateText,
                color = Color.White.copy(alpha = 0.65f),
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Image(
            painter = painterResource(id = weather.weatherType.getIconRes(isDay = true)),
            contentDescription = weather.weatherType.weatherDesc,
            modifier = Modifier.size(40.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = weather.weatherType.weatherDesc,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp,
            )
            if (weather.precipitationProbabilityPercent > 0) {
                Text(
                    text = "${weather.precipitationProbabilityPercent}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Cyan,
                    textAlign = TextAlign.End,
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text =
                "${weather.minimumTemperature.roundToInt()}° / " +
                    "${weather.maximumTemperature.roundToInt()}°",
            modifier = Modifier.widthIn(min = 72.dp),
            color = Color.White,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
        )
    }
}
