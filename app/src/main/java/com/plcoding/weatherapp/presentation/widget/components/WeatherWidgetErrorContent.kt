package com.plcoding.weatherapp.presentation.widget.components

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.presentation.MainActivity

@Composable
fun WeatherWidgetErrorContent() {
    Box(
        modifier =
            GlanceModifier
                .fillMaxSize()
                .background(ImageProvider(R.drawable.bg_widget))
                .clickable(actionStartActivity<MainActivity>()),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Error loading weather. Tap to retry.",
            style = TextStyle(color = WhiteColorProvider),
        )
    }
}
