package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgentStatus
import com.example.ui.components.GlassPanel
import com.example.ui.components.NovaTopBar
import com.example.ui.components.StatusDot
import com.example.ui.navigation.NovaNavTarget
import com.example.ui.theme.NovaAlertDanger
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaBackgroundElevated
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPanelBorderBright
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

@Composable
fun DashboardScreen(
    viewModel: NovaViewModel,
    modifier: Modifier = Modifier
) {
    val deviceState by viewModel.deviceState.collectAsState()
    val worldState by viewModel.worldState.collectAsState()
    val agents by viewModel.agents.collectAsState()
    val trustLevel by viewModel.trustLevel.collectAsState()
    val activities by viewModel.recentActivities.collectAsState()

    val runningCount = agents.count { it.status == AgentStatus.RUNNING }
    val totalCount = agents.size

    val batteryTemp = deviceState.battery.temperatureC
    val batteryColor = when {
        batteryTemp > 55f -> NovaAlertDanger
        batteryTemp > 45f -> NovaWarning
        else -> NovaPrimaryAccent
    }

    val currentDateStr = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US).format(Date())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackgroundBase)
            .testTag("dashboard_screen")
    ) {
        // Top Bar
        NovaTopBar(
            title = "NOVA",
            subtitle = "SYSTEM ONLINE",
            onRefreshClick = { viewModel.refreshWorldData() },
            onSearchClick = { viewModel.navigateTo(NovaNavTarget.COMMAND_LINE) },
            onNotificationsClick = { viewModel.navigateTo(NovaNavTarget.PERMISSIONS) }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Row of 3 Stat Tiles (REAL DATA)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Tile 1: Agents
                    StatTile(
                        title = "AGENTS",
                        value = String.format(Locale.ROOT, "%02d/%02d", runningCount, totalCount),
                        subtext = "Active Now",
                        accentColor = NovaPrimaryAccent,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(NovaNavTarget.AGENT_PANEL) }
                    )

                    // Tile 2: Trust
                    StatTile(
                        title = "TRUST",
                        value = "$trustLevel%",
                        subtext = "Verified Nominal",
                        accentColor = NovaSecondaryAccent,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(NovaNavTarget.PERMISSIONS) }
                    )

                    // Tile 3: Battery (REAL)
                    StatTile(
                        title = "BATTERY",
                        value = "${deviceState.battery.levelPercent}%",
                        subtext = "${batteryTemp.toInt()}°C · ${deviceState.battery.chargingStatus.take(4)}",
                        accentColor = batteryColor,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(NovaNavTarget.DEVICE_SCAN) }
                    )
                }
            }

            // "Today" Hero Card
            item {
                GlassPanel(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NovaPanelBorderBright.copy(alpha = 0.5f),
                    borderWidth = 1.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0x2200FF88),
                                        Color(0x1000E5FF),
                                        Color(0x05000000)
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentDateStr.uppercase(),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.sp,
                                    color = NovaPrimaryAccent
                                )
                                Text(
                                    text = "MISSION CONTROL",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = NovaTextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Better decisions. A calmer you.",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = NovaTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "All agents synchronized with real-time neural telemetry.",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 12.sp,
                                color = NovaTextSecondary
                            )
                        }
                    }
                }
            }

            // LIVE WORLD WIDGET (Open-Meteo Weather, AQI, CoinGecko Crypto)
            item {
                GlassPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo(NovaNavTarget.WORLD) },
                    borderColor = NovaPanelBorder,
                    showScanlines = false
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CloudQueue,
                                    contentDescription = "World Feed",
                                    tint = NovaSecondaryAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LIVE WORLD TELEMETRY",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NovaSecondaryAccent
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (worldState.isFetching) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(12.dp),
                                        strokeWidth = 2.dp,
                                        color = NovaPrimaryAccent
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = "Tap for World Feed >",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = NovaTextTertiary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Middle row: Weather + AQI
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Weather
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${worldState.weather.temperatureC.toInt()}°C",
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp,
                                        color = NovaTextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = worldState.weather.condition,
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 13.sp,
                                        color = NovaTextSecondary
                                    )
                                }
                                Text(
                                    text = "${worldState.weather.cityName} • Humidity ${worldState.weather.humidityPercent}%",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = NovaTextTertiary
                                )
                            }

                            // AQI
                            Box(
                                modifier = Modifier
                                    .background(NovaBackgroundElevated, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Air,
                                            contentDescription = "AQI",
                                            tint = NovaPrimaryAccent,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "AQI ${worldState.airQuality.aqi}",
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = NovaPrimaryAccent
                                        )
                                    }
                                    Text(
                                        text = worldState.airQuality.status,
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 10.sp,
                                        color = NovaTextSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom row: Crypto BTC
                        val btc = worldState.cryptos.firstOrNull { it.symbol == "BTC" }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0x330A0E14), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CurrencyBitcoin,
                                    contentDescription = "BTC",
                                    tint = NovaWarning,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "BTC/USD: $${String.format(Locale.US, "%,.0f", btc?.priceUsd ?: 62450.0)}",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NovaTextPrimary
                                )
                            }

                            val change = btc?.change24h ?: 2.34
                            Text(
                                text = (if (change >= 0) "+" else "") + "$change%",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (change >= 0) NovaPrimaryAccent else NovaAlertDanger
                            )
                        }
                    }
                }
            }

            // "Device Vitals" card (REAL DATA)
            item {
                GlassPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo(NovaNavTarget.DEVICE_SCAN) },
                    borderColor = NovaPanelBorder
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Memory,
                                    contentDescription = "Device Vitals",
                                    tint = NovaPrimaryAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "DEVICE VITALS (REAL TELEMETRY)",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NovaPrimaryAccent
                                )
                            }
                            Text(
                                text = "Full Scan >",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = NovaPrimaryAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Row 1: CPU
                        VitalRow(
                            label = "CPU Usage",
                            value = "${deviceState.cpu.usagePercent}% · ${deviceState.cpu.temperatureC.toInt()}°C",
                            detail = "${deviceState.cpu.cores} Cores (${deviceState.cpu.model.take(12)})",
                            isWarning = deviceState.cpu.usagePercent > 80
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Row 2: RAM
                        VitalRow(
                            label = "RAM Memory",
                            value = "${deviceState.memory.usagePercent}%",
                            detail = "${deviceState.memory.availableMb / 1024} GB Free / ${deviceState.memory.totalMb / 1024} GB",
                            isWarning = deviceState.memory.usagePercent > 80
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Row 3: Storage
                        VitalRow(
                            label = "Internal Storage",
                            value = "${deviceState.storage.usagePercent}%",
                            detail = "${deviceState.storage.freeGb} GB Free / ${deviceState.storage.totalGb} GB",
                            isWarning = deviceState.storage.usagePercent > 90
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Row 4: Network
                        VitalRow(
                            label = "Network",
                            value = deviceState.network.type,
                            detail = "${deviceState.network.downloadSpeedMbps} Mbps · ${deviceState.network.signalDbm} dBm",
                            isWarning = false
                        )
                    }
                }
            }

            // Quick Modules section (2x2 grid)
            item {
                Text(
                    text = "QUICK MODULES",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovaTextSecondary,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickModuleCard(
                            title = "LifeGraph",
                            subtitle = "Your life. Connected.",
                            icon = Icons.Default.Sensors,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.navigateTo(NovaNavTarget.LIFEGRAPH) }
                        )
                        QuickModuleCard(
                            title = "VisionCore",
                            subtitle = "See. Understand.",
                            icon = Icons.Default.Visibility,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.navigateTo(NovaNavTarget.VISIONCORE) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickModuleCard(
                            title = "CommandLine",
                            subtitle = "Control. Automate.",
                            icon = Icons.Default.Terminal,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.navigateTo(NovaNavTarget.COMMAND_LINE) }
                        )
                        QuickModuleCard(
                            title = "Agent Panel",
                            subtitle = "Your AI team.",
                            icon = Icons.Default.SmartToy,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.navigateTo(NovaNavTarget.AGENT_PANEL) }
                        )
                    }
                }
            }

            // Recent Activity section
            item {
                Text(
                    text = "RECENT ACTIVITY",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovaTextSecondary,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        activities.take(3).forEachIndexed { index, act ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(NovaPrimaryAccent.copy(alpha = 0.12f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (act.iconType) {
                                            "cloud" -> Icons.Default.CloudQueue
                                            "agent" -> Icons.Default.SmartToy
                                            else -> Icons.Default.Bolt
                                        },
                                        contentDescription = null,
                                        tint = NovaPrimaryAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = act.title,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = NovaTextPrimary
                                    )
                                    Text(
                                        text = act.description,
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 11.sp,
                                        color = NovaTextSecondary,
                                        maxLines = 1
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = act.timestamp,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = NovaTextTertiary
                                )
                            }
                            if (index < 2) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(NovaPanelBorder.copy(alpha = 0.5f))
                                )
                            }
                        }
                    }
                }
            }

            // Extra space at bottom for smooth scrolling
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StatTile(
    title: String,
    value: String,
    subtext: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassPanel(
        modifier = modifier.clickable { onClick() },
        borderColor = NovaPanelBorder
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = title,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = NovaTextSecondary,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtext,
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                color = NovaTextTertiary,
                maxLines = 1
            )
        }
    }
}

@Composable
fun VitalRow(
    label: String,
    value: String,
    detail: String,
    isWarning: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label,
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.sp,
                color = NovaTextPrimary
            )
            Text(
                text = detail,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = NovaTextTertiary
            )
        }
        Text(
            text = value,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = if (isWarning) NovaWarning else NovaPrimaryAccent
        )
    }
}

@Composable
fun QuickModuleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassPanel(
        modifier = modifier.clickable { onClick() },
        borderColor = NovaPanelBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(NovaPrimaryAccent.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = NovaPrimaryAccent,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NovaTextPrimary
                )
                Text(
                    text = subtitle,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    color = NovaTextSecondary,
                    maxLines = 1
                )
            }
        }
    }
}
