package com.plcoding.weatherapp.presentation.weather.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.domain.weather.DailyWeatherData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun DailyWeatherItem( weather: DailyWeatherData, modifier: Modifier = Modifier) {

    val today = LocalDate.now()

   val dailyText = when (weather.date){
       today -> "today"
       today.plusDays(1) -> "Tomorrow"
       else -> weather.date.format(
           DateTimeFormatter.ofPattern(
               "EEE",
               LocalLocale.current.platformLocale
           ),
       )
   }
    Row(modifier = modifier.fillMaxWidth().heightIn(min = 64.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = dailyText,
            modifier = Modifier.width(88.dp),
            color = Color.White
        )
        Image(
            painter = painterResource(id = weather.weatherType.getIconRes(isDay = true),),
            contentDescription = weather.weatherType.weatherDesc,
            modifier = Modifier.size(40.dp)
        )

    Spacer(modifier = Modifier.width(12.dp))
    Text(
        text = weather.weatherType.weatherDesc,
        modifier = Modifier.weight(1f),
        color = Color.White,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
        if (weather.precipitationProbabilityPercent > 0){
            Text(
                text = "${weather.precipitationProbabilityPercent}%",
                modifier = Modifier.width(44.dp),
                color = Color.Cyan,
                textAlign = TextAlign.End
            )
        }
        Text(
            text ="${weather.minimumTemperature.roundToInt()}° / " +
                "${weather.maximumTemperature.roundToInt()}°",
            modifier = Modifier.width(76.dp),
            color = Color.White,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Medium,
        )

    }

}
