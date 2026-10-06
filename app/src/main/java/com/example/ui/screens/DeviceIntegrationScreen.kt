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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ConnectedDeviceItem
import com.example.ui.components.GlassPanel
import com.example.ui.components.NovaButton
import com.example.ui.components.NovaTopBar
import com.example.ui.components.StatusDot
import com.example.ui.components.StatusDotType
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.theme.NovaTextTertiary
import com.example.viewmodel.NovaViewModel

@Composable
fun DeviceIntegrationScreen(
    viewModel: NovaViewModel,
    modifier: Modifier = Modifier
) {
    val devices by viewModel.connectedDevices.collectAsState()
    val isSyncEnabled by viewModel.isMultiDeviceSyncEnabled.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackgroundBase)
            .testTag("device_integration_screen")
    ) {
        // Top Bar
        NovaTopBar(
            title = "Devices",
            subtitle = "Peripherals & Mesh Network",
            onBackClick = { viewModel.navigateBack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "HARDWARE MESH TOPOLOGY",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovaTextSecondary,
                    letterSpacing = 1.sp
                )
            }

            items(devices, key = { it.id }) { dev ->
                DeviceItemRow(device = dev)
            }

            // Multi-Device Sync card
            item {
                Spacer(modifier = Modifier.height(14.dp))
                GlassPanel(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NovaPrimaryAccent.copy(alpha = 0.4f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                tint = NovaPrimaryAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "MULTI-DEVICE SYNC",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = NovaPrimaryAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Keep your data and agents in sync across all your devices with end-to-end cryptographic integrity.",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = NovaTextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        NovaButton(
                            onClick = { viewModel.toggleMultiDeviceSync() },
                            isOutlined = !isSyncEnabled,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isSyncEnabled) "SYNC ACTIVE (P2P ENCRYPTED)" else "ENABLE SYNC",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceItemRow(device: ConnectedDeviceItem) {
    val icon: ImageVector = when {
        device.name.contains("Phone", ignoreCase = true) -> Icons.Default.PhoneAndroid
        device.name.contains("Laptop", ignoreCase = true) -> Icons.Default.Laptop
        device.name.contains("Watch", ignoreCase = true) -> Icons.Default.Watch
        device.name.contains("Earbuds", ignoreCase = true) -> Icons.Default.Headphones
        device.name.contains("Glasses", ignoreCase = true) -> Icons.Default.Visibility
        device.name.contains("Car", ignoreCase = true) -> Icons.Default.DirectionsCar
        else -> Icons.Default.Home
    }

    GlassPanel(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (device.isConnected) NovaPanelBorder else NovaPanelBorder.copy(alpha = 0.4f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            if (device.isConnected) NovaPrimaryAccent.copy(alpha = 0.12f) else NovaTextTertiary.copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = device.name,
                        tint = if (device.isConnected) NovaPrimaryAccent else NovaTextTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = device.name,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (device.isConnected) NovaTextPrimary else NovaTextSecondary
                        )
                        if (device.isCurrentDevice) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(NovaPrimaryAccent.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "THIS DEVICE",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NovaPrimaryAccent
                                )
                            }
                        }
                    }
                    Text(
                        text = device.platform + (if (device.batteryPercent != null) " · ${device.batteryPercent}%" else ""),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = NovaTextTertiary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                StatusDot(
                    size = 6.dp,
                    type = if (device.isConnected) StatusDotType.ACTIVE else StatusDotType.WAITING
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (device.isConnected) "Connected" else "Not Connected",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = if (device.isConnected) NovaPrimaryAccent else NovaTextTertiary
                )
            }
        }
    }
}
