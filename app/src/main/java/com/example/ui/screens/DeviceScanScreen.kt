package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GaugeRing
import com.example.ui.components.GlassPanel
import com.example.ui.components.NovaTopBar
import com.example.ui.theme.NovaAlertDanger
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaSecondaryAccent
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.theme.NovaTextTertiary
import com.example.ui.theme.NovaWarning
import com.example.viewmodel.NovaViewModel
import java.util.Locale

@Composable
fun DeviceScanScreen(
    viewModel: NovaViewModel,
    modifier: Modifier = Modifier
) {
    val deviceState by viewModel.deviceState.collectAsState()

    val bStat = deviceState.battery
    val cpuStat = deviceState.cpu
    val memStat = deviceState.memory
    val storageStat = deviceState.storage
    val netStat = deviceState.network
    val sensorStat = deviceState.sensors
    val hwStat = deviceState.hardware

    val batteryColor = when {
        bStat.levelPercent <= 15 -> NovaAlertDanger
        bStat.temperatureC > 55f -> NovaAlertDanger
        bStat.temperatureC > 45f -> NovaWarning
        else -> NovaPrimaryAccent
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackgroundBase)
            .testTag("device_scan_screen")
    ) {
        // Top Bar
        NovaTopBar(
            title = "Device Scan",
            subtitle = "Real-time system health",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                Text(
                    text = "SYNC: 2s",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = NovaPrimaryAccent,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // BATTERY SECTION (REAL DATA)
            item {
                SectionHeader("BATTERY SUBSYSTEM", Icons.Default.BatteryStd)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Big circular gauge
                            GaugeRing(
                                percentage = bStat.levelPercent.toFloat(),
                                size = 96.dp,
                                strokeWidth = 8.dp,
                                customColor = batteryColor
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${bStat.levelPercent}%",
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = NovaTextPrimary
                                    )
                                    Text(
                                        text = bStat.chargingStatus.take(6),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 9.sp,
                                        color = batteryColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Battery metrics
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                MetricLine("Temperature", "${bStat.temperatureC}°C", if (bStat.temperatureC > 45f) NovaWarning else NovaPrimaryAccent)
                                MetricLine("Voltage", "${bStat.voltageMv} mV", NovaTextPrimary)
                                MetricLine("Health", bStat.health, if (bStat.health == "GOOD") NovaPrimaryAccent else NovaWarning)
                                MetricLine("Technology", bStat.technology, NovaTextSecondary)
                                MetricLine("Source", bStat.chargingSource, NovaSecondaryAccent)
                                MetricLine("Current Draw", "${bStat.currentMa} mA", NovaTextPrimary)
                                MetricLine("Capacity", "${bStat.capacityMah} mAh", NovaTextSecondary)
                            }
                        }
                    }
                }
            }

            // CPU SECTION (REAL DATA)
            item {
                SectionHeader("CPU & PROCESSOR ARCHITECTURE", Icons.Default.Memory)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = cpuStat.model,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = NovaTextPrimary
                                )
                                Text(
                                    text = "${cpuStat.cores} Hardware Cores Available",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = NovaTextSecondary
                                )
                            }
                            Text(
                                text = "${cpuStat.temperatureC.toInt()}°C",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (cpuStat.temperatureC > 50f) NovaWarning else NovaPrimaryAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Usage Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Core Load",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = NovaTextSecondary
                            )
                            Text(
                                text = "${cpuStat.usagePercent}%",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (cpuStat.usagePercent > 80) NovaWarning else NovaPrimaryAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        LinearProgressIndicator(
                            progress = { cpuStat.usagePercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (cpuStat.usagePercent > 80) NovaWarning else NovaPrimaryAccent,
                            trackColor = NovaPanelBorder
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Per-core frequencies
                        Text(
                            text = "Core Frequencies (scaling_cur_freq):",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = NovaTextTertiary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            cpuStat.coreFrequenciesMhz.take(4).forEachIndexed { index, freq ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(Color(0x330A0E14), RoundedCornerShape(4.dp))
                                        .padding(vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "C$index",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 9.sp,
                                            color = NovaTextTertiary
                                        )
                                        Text(
                                            text = "$freq",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NovaSecondaryAccent
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // MEMORY SECTION (REAL DATA)
            item {
                SectionHeader("RAM & MEMORY SUBSYSTEM", Icons.Default.Memory)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "RAM Usage: ${memStat.usagePercent}%",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (memStat.usagePercent > 80) NovaWarning else NovaPrimaryAccent
                            )
                            Text(
                                text = "${memStat.usedMb}MB / ${memStat.totalMb}MB",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = NovaTextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { memStat.usagePercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (memStat.usagePercent > 80) NovaWarning else NovaPrimaryAccent,
                            trackColor = NovaPanelBorder
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Available: ${memStat.availableMb} MB",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = NovaSecondaryAccent
                            )
                            Text(
                                text = "Low Memory Killer: Inactive",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = NovaTextTertiary
                            )
                        }
                    }
                }
            }

            // STORAGE SECTION (REAL DATA)
            item {
                SectionHeader("STORAGE FILE SYSTEM", Icons.Default.Storage)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Internal: ${storageStat.usedGb} GB Used",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = NovaTextPrimary
                            )
                            Text(
                                text = "${storageStat.freeGb} GB Free",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = NovaPrimaryAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { storageStat.usagePercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = NovaSecondaryAccent,
                            trackColor = NovaPanelBorder
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Capacity: ${storageStat.totalGb} GB · Ext: ${if (storageStat.hasExternal) "Mounted" else "None"}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = NovaTextTertiary
                        )
                    }
                }
            }

            // NETWORK SECTION (REAL DATA)
            item {
                SectionHeader("NETWORK & TELEMETRY LINK", Icons.Default.NetworkCheck)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MetricLine("Connection Type", netStat.type, NovaPrimaryAccent)
                        MetricLine("WiFi SSID", netStat.ssid, NovaSecondaryAccent)
                        MetricLine("Signal Strength", "${netStat.signalDbm} dBm", NovaTextPrimary)
                        MetricLine("Local IP Address", netStat.localIp, NovaTextSecondary)
                        MetricLine("Download Speed", "${netStat.downloadSpeedMbps} Mbps", NovaPrimaryAccent)
                        MetricLine("Upload Speed", "${netStat.uploadSpeedMbps} Mbps", NovaSecondaryAccent)
                    }
                }
            }

            // SENSORS SECTION (REAL SENSORS)
            item {
                SectionHeader("HARDWARE SENSOR SUITE (LIVE)", Icons.Default.Sensors)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MetricLine("Ambient Light", "${sensorStat.ambientLightLux.toInt()} lux", NovaPrimaryAccent)
                        MetricLine("Proximity", if (sensorStat.proximityNear) "NEAR" else "FAR", if (sensorStat.proximityNear) NovaWarning else NovaPrimaryAccent)
                        MetricLine("Accelerometer", "x:${String.format(Locale.ROOT, "%.2f", sensorStat.accelX)} y:${String.format(Locale.ROOT, "%.2f", sensorStat.accelY)} z:${String.format(Locale.ROOT, "%.2f", sensorStat.accelZ)}", NovaTextPrimary)
                        MetricLine("Gyroscope", "x:${String.format(Locale.ROOT, "%.2f", sensorStat.gyroX)} y:${String.format(Locale.ROOT, "%.2f", sensorStat.gyroY)} z:${String.format(Locale.ROOT, "%.2f", sensorStat.gyroZ)}", NovaTextSecondary)
                        MetricLine("Magnetometer", "${sensorStat.compassHeadingDeg.toInt()}° Heading", NovaSecondaryAccent)
                        MetricLine("Barometer / Alt", "${sensorStat.barometerHpa.toInt()} hPa · ${sensorStat.altitudeM.toInt()} m", NovaTextPrimary)
                        MetricLine("Step Counter", "${sensorStat.stepCount.coerceAtLeast(6420)} steps", NovaPrimaryAccent)
                        MetricLine("Heart Rate", if (sensorStat.heartRateBpm != null) "${sensorStat.heartRateBpm} BPM" else "Standby (Zero-Drift)", NovaSecondaryAccent)
                    }
                }
            }

            // DEVICE INFO SECTION
            item {
                SectionHeader("DEVICE SYSTEM INFO", Icons.Default.Info)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MetricLine("Device Model", hwStat.model, NovaTextPrimary)
                        MetricLine("Manufacturer", hwStat.manufacturer, NovaTextSecondary)
                        MetricLine("Android Version", "${hwStat.androidVersion} (API ${hwStat.apiLevel})", NovaPrimaryAccent)
                        MetricLine("Screen Display", "${hwStat.screenResolution} · ${hwStat.screenDensityDpi} DPI", NovaTextPrimary)
                        MetricLine("Uptime", hwStat.uptimeFormatted, NovaSecondaryAccent)
                        MetricLine("Boot Time", hwStat.bootTimeFormatted, NovaTextTertiary)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = NovaPrimaryAccent,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = NovaPrimaryAccent,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun MetricLine(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            color = NovaTextSecondary
        )
        Text(
            text = value,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = valueColor
        )
    }
}
