package com.plcoding.weatherapp.presentation.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.presentation.MainActivity

@Composable
fun NoLocationContent() {
    Box(
        modifier = GlanceModifier.fillMaxSize().background(ImageProvider(R.drawable.bg_widget))
            .clickable(
                actionStartActivity<MainActivity>(),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No location data",
                style =
                    TextStyle(
                        color = WhiteColorProvider,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    ),
            )
            Spacer(
                modifier = GlanceModifier.height(8.dp),
            )
            Text(
                text = "Tap to open the app",
                style =
                    TextStyle(
                        color = WhiteColorProvider,
                        fontSize = 12.sp,
                    ),
            )
        }
    }
}
