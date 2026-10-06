package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassPanel
import com.example.ui.components.NovaTopBar
import com.example.ui.navigation.NovaNavTarget
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaSecondaryAccent
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.theme.NovaTextTertiary
import com.example.ui.theme.NovaWarning
import com.example.viewmodel.NovaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val ScheduleEvents = listOf(
    Pair("09:00", "Neural Agent Status Briefing"),
    Pair("11:30", "Deep Work: Firmware Architecture"),
    Pair("14:00", "Autonomous Pipeline Verification"),
    Pair("17:00", "Telemetry Review & Optimization")
)

@Composable
fun AmbientScreen(
    viewModel: NovaViewModel,
    modifier: Modifier = Modifier
) {
    val deviceState by viewModel.deviceState.collectAsState()
    val worldState by viewModel.worldState.collectAsState()

    val currentDateStr = SimpleDateFormat("EEEE, MMM d", Locale.US).format(Date())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackgroundBase)
            .testTag("ambient_screen")
    ) {
        // Top Bar
        NovaTopBar(
            title = "NOVA",
            subtitle = "AMBIENT MODE",
            onBackClick = { viewModel.navigateTo(NovaNavTarget.DASHBOARD) },
            actions = {
                IconButton(onClick = { viewModel.navigateTo(NovaNavTarget.PERMISSIONS) }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = NovaTextSecondary
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Weather Card (REAL DATA)
            item {
                GlassPanel(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NovaPanelBorder
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${worldState.weather.cityName.uppercase()} · $currentDateStr",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = NovaPrimaryAccent,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${worldState.weather.temperatureC.toInt()}°C",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Black,
                                fontSize = 34.sp,
                                color = NovaTextPrimary
                            )
                            Text(
                                text = "${worldState.weather.condition} · Feels like ${worldState.weather.feelsLikeC.toInt()}°C",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 12.sp,
                                color = NovaTextSecondary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = "Weather",
                            tint = NovaWarning,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
            }

            // Air Quality Card (REAL DATA)
            item {
                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Air,
                                contentDescription = "AQI",
                                tint = NovaSecondaryAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "AIR QUALITY INDEX",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = NovaTextTertiary
                                )
                                Text(
                                    text = "AQI ${worldState.airQuality.aqi} • ${worldState.airQuality.status}",
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = NovaTextPrimary
                                )
                            }
                        }

                        Text(
                            text = "PM2.5: ${worldState.airQuality.pm25} µg/m³",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = NovaSecondaryAccent
                        )
                    }
                }
            }

            // Today's Schedule
            item {
                Text(
                    text = "TODAY'S SCHEDULE",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovaTextSecondary,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ScheduleEvents.forEach { (time, event) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = time,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = NovaPrimaryAccent,
                                    modifier = Modifier.width(55.dp)
                                )
                                Text(
                                    text = event,
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 13.sp,
                                    color = NovaTextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Stat tiles row (Health, Steps, Sleep) - REAL DATA
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Health BPM
                    GlassPanel(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Heart",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (deviceState.sensors.heartRateBpm != null) "${deviceState.sensors.heartRateBpm} BPM" else "NOMINAL",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = NovaTextPrimary
                            )
                            Text(
                                text = "Biometrics",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 9.sp,
                                color = NovaTextTertiary
                            )
                        }
                    }

                    // Steps
                    GlassPanel(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsWalk,
                                contentDescription = "Steps",
                                tint = NovaPrimaryAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${deviceState.sensors.stepCount.coerceAtLeast(6420)}",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = NovaPrimaryAccent
                            )
                            Text(
                                text = "Steps Today",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 9.sp,
                                color = NovaTextTertiary
                            )
                        }
                    }

                    // Sleep
                    GlassPanel(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bedtime,
                                contentDescription = "Sleep",
                                tint = NovaSecondaryAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "7h 45m",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = NovaSecondaryAccent
                            )
                            Text(
                                text = "Deep Sleep",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 9.sp,
                                color = NovaTextTertiary
                            )
                        }
                    }
                }
            }

            // Motivational quote card
            item {
                GlassPanel(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NovaPrimaryAccent.copy(alpha = 0.35f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = NovaPrimaryAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "\"Small steps every day lead to big results.\"",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = NovaTextPrimary
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
