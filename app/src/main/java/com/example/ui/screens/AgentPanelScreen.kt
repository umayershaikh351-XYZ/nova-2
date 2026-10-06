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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.Agent
import com.example.data.model.AgentStatus
import com.example.ui.components.GlassPanel
import com.example.ui.components.NovaButton
import com.example.ui.components.NovaTopBar
import com.example.ui.components.StatusDot
import com.example.ui.components.StatusDotType
import com.example.ui.navigation.NovaNavTarget
import com.example.ui.theme.NovaAlertDanger
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaSecondaryAccent
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.theme.NovaTextTertiary
import com.example.ui.theme.NovaWarning
import com.example.viewmodel.NovaViewModel

@Composable
fun AgentPanelScreen(
    viewModel: NovaViewModel,
    modifier: Modifier = Modifier
) {
    val agents by viewModel.agents.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackgroundBase)
            .testTag("agent_panel_screen")
    ) {
        // Top Bar
        NovaTopBar(
            title = "Agent Panel",
            subtitle = "${agents.size} Active Agents",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Alerts",
                        tint = NovaPrimaryAccent
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(agents, key = { it.id }) { agent ->
                AgentCard(
                    agent = agent,
                    onToggle = { viewModel.toggleAgentStatus(agent.id) }
                )
            }
        }

        // Bottom 2 buttons: "Pause All" & "Kill Switch"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NovaButton(
                onClick = { viewModel.pauseAllAgents() },
                isOutlined = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("pause_all_button")
            ) {
                Text(
                    text = "PAUSE ALL",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            NovaButton(
                onClick = { viewModel.navigateTo(NovaNavTarget.KILL_SWITCH) },
                isDanger = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("kill_switch_button")
            ) {
                Text(
                    text = "KILL SWITCH",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AgentCard(
    agent: Agent,
    onToggle: () -> Unit
) {
    val icon: ImageVector = when (agent.iconCategory) {
        "research" -> Icons.Default.Search
        "code" -> Icons.Default.Code
        "travel" -> Icons.Default.Flight
        "health" -> Icons.Default.DirectionsRun
        "finance" -> Icons.Default.ShowChart
        "creative" -> Icons.Default.AutoAwesome
        "security" -> Icons.Default.Security
        else -> Icons.Default.SmartToy
    }

    val statusColor = when (agent.status) {
        AgentStatus.RUNNING -> NovaPrimaryAccent
        AgentStatus.ACTIVE -> NovaSecondaryAccent
        AgentStatus.WAITING -> NovaWarning
        AgentStatus.IDLE -> NovaTextTertiary
        AgentStatus.DONE -> Color(0xFF64B5F6)
    }

    GlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular icon with colored background
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(statusColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = agent.name,
                    tint = statusColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Center details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = agent.name,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NovaTextPrimary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Status Badge
                    Box(
                        modifier = Modifier
                            .background(statusColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = agent.status.label,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = statusColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Live transcript
                Text(
                    text = agent.transcript,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = NovaTextSecondary,
                    maxLines = 2,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right side: spinner or status dot + dismiss/toggle
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (agent.status == AgentStatus.RUNNING) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = NovaPrimaryAccent
                    )
                } else {
                    StatusDot(
                        size = 6.dp,
                        type = when (agent.status) {
                            AgentStatus.RUNNING -> StatusDotType.ACTIVE
                            AgentStatus.WAITING -> StatusDotType.WAITING
                            else -> StatusDotType.ACTIVE
                        }
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onToggle,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Toggle Agent",
                        tint = NovaTextTertiary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
