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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Security
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
import com.example.data.model.PermissionItem
import com.example.data.model.PermissionState
import com.example.ui.components.GlassPanel
import com.example.ui.components.NovaButton
import com.example.ui.components.NovaTopBar
import com.example.ui.theme.NovaAlertDanger
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.theme.NovaTextTertiary
import com.example.ui.theme.NovaWarning
import com.example.viewmodel.NovaViewModel

@Composable
fun PermissionScreen(
    viewModel: NovaViewModel,
    modifier: Modifier = Modifier
) {
    val permissions by viewModel.permissions.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackgroundBase)
            .testTag("permission_screen")
    ) {
        // Top Bar
        NovaTopBar(
            title = "Permissions",
            subtitle = "Manage what NOVA can access.",
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
                    text = "ACCESS CONTROLS (TAP TO CYCLE)",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovaTextSecondary,
                    letterSpacing = 1.sp
                )
            }

            items(permissions, key = { it.id }) { item ->
                PermissionRow(
                    item = item,
                    onToggle = { viewModel.cyclePermission(item.id) }
                )
            }

            // High-Risk Actions
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "HIGH-RISK ACTIONS",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovaAlertDanger,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NovaAlertDanger.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(NovaAlertDanger.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = NovaAlertDanger,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Payment / Account Changes",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = NovaTextPrimary
                            )
                            Text(
                                text = "Requires your explicit biometrics and approval",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 11.sp,
                                color = NovaTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Bottom: "Manage Advanced Permissions" button
        Box(modifier = Modifier.padding(16.dp)) {
            NovaButton(
                onClick = {},
                isOutlined = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "MANAGE ADVANCED PERMISSIONS",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun PermissionRow(
    item: PermissionItem,
    onToggle: () -> Unit
) {
    val icon: ImageVector = when (item.iconName) {
        "Camera" -> Icons.Default.CameraAlt
        "Mic" -> Icons.Default.Mic
        "Location" -> Icons.Default.LocationOn
        "Folder" -> Icons.Default.Folder
        "Calendar" -> Icons.Default.CalendarMonth
        "Contacts" -> Icons.Default.Contacts
        "Email" -> Icons.Default.Email
        else -> Icons.Default.Payments
    }

    val pillColor = when (item.state) {
        PermissionState.ALLOWED -> NovaPrimaryAccent
        PermissionState.LIMITED -> NovaWarning
        PermissionState.BLOCKED -> NovaAlertDanger
    }

    GlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        borderColor = NovaPanelBorder
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
                        .size(36.dp)
                        .background(pillColor.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = item.name,
                        tint = pillColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = item.name,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NovaTextPrimary
                    )
                    Text(
                        text = item.description,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 11.sp,
                        color = NovaTextTertiary,
                        maxLines = 1
                    )
                }
            }

            // Status Pill
            Box(
                modifier = Modifier
                    .background(pillColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = item.state.label.uppercase(),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = pillColor
                )
            }
        }
    }
}
