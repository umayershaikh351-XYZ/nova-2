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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassPanel
import com.example.ui.components.NovaButton
import com.example.ui.components.NovaTopBar
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaSecondaryAccent
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.theme.NovaTextTertiary
import com.example.viewmodel.NovaViewModel

@Composable
fun LifeGraphScreen(
    viewModel: NovaViewModel,
    modifier: Modifier = Modifier
) {
    val goals by viewModel.goals.collectAsState()
    val habits by viewModel.habits.collectAsState()
    val relationships by viewModel.relationships.collectAsState()
    val memories by viewModel.memories.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Goals", "Habits", "Relationships", "Preferences")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackgroundBase)
            .testTag("lifegraph_screen")
    ) {
        // Top Bar
        NovaTopBar(
            title = "LifeGraph",
            subtitle = "Your goals, habits, relationships & more.",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = NovaTextSecondary
                    )
                }
            }
        )

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tabs.forEachIndexed { index, tabName ->
                val isSelected = selectedTabIndex == index
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { selectedTabIndex = index }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = tabName.uppercase(),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp,
                        letterSpacing = 0.8.sp,
                        color = if (isSelected) NovaPrimaryAccent else NovaTextTertiary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(2.dp)
                            .background(if (isSelected) NovaPrimaryAccent else Color.Transparent)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedTabIndex) {
                0 -> {
                    // Goals Tab
                    item {
                        Text(
                            text = "STRATEGIC GOALS",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NovaTextSecondary,
                            letterSpacing = 1.sp
                        )
                    }

                    items(goals) { goal ->
                        GlassPanel(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(NovaPrimaryAccent.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when {
                                            goal.title.contains("Cyber", ignoreCase = true) -> Icons.Default.Security
                                            goal.title.contains("NOVA", ignoreCase = true) -> Icons.Default.Code
                                            goal.title.contains("Fitness", ignoreCase = true) -> Icons.Default.DirectionsRun
                                            goal.title.contains("Finance", ignoreCase = true) -> Icons.Default.ShowChart
                                            else -> Icons.Default.FlightTakeoff
                                        },
                                        contentDescription = null,
                                        tint = NovaPrimaryAccent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = goal.title,
                                            fontFamily = FontFamily.SansSerif,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = NovaTextPrimary
                                        )
                                        Text(
                                            text = "${(goal.progress * 100).toInt()}%",
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = NovaPrimaryAccent
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    LinearProgressIndicator(
                                        progress = { goal.progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(5.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = NovaPrimaryAccent,
                                        trackColor = NovaPanelBorder
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Target: ${goal.targetDate} · Category: ${goal.category}",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = NovaTextTertiary
                                    )
                                }
                            }
                        }
                    }

                    // Recent Memories section
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "RECENT MEMORIES",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NovaTextSecondary,
                            letterSpacing = 1.sp
                        )
                    }

                    items(memories) { mem ->
                        GlassPanel(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(NovaSecondaryAccent.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = NovaSecondaryAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = mem.title,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = NovaTextPrimary
                                    )
                                    Text(
                                        text = mem.summary,
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 11.sp,
                                        color = NovaTextSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = mem.timestamp,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = NovaTextTertiary
                                )
                            }
                        }
                    }
                }
                1 -> {
                    // Habits Tab
                    items(habits) { habit ->
                        GlassPanel(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = habit.name,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = NovaTextPrimary
                                    )
                                    Text(
                                        text = "${habit.streakDays} Day Streak",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        color = NovaPrimaryAccent
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Completed",
                                    tint = if (habit.completedToday) NovaPrimaryAccent else NovaTextTertiary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
                2 -> {
                    // Relationships Tab
                    items(relationships) { rel ->
                        GlassPanel(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = rel.name,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = NovaTextPrimary
                                    )
                                    Text(
                                        text = "${rel.role} · Last Contact: ${rel.lastContact}",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = NovaTextSecondary
                                    )
                                }
                                Text(
                                    text = "Trust: ${rel.trustScore}%",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = NovaSecondaryAccent
                                )
                            }
                        }
                    }
                }
                else -> {
                    // Preferences Tab
                    item {
                        GlassPanel(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "AUTONOMOUS AGENT PREFERENCES",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = NovaPrimaryAccent
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "• Proactive schedule adjustments enabled\n• Real-time biometric anomaly alerts active\n• Zero-trust security clearance level: 4/5",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    lineHeight = 18.sp,
                                    color = NovaTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom full width button
        Box(modifier = Modifier.padding(16.dp)) {
            NovaButton(
                onClick = {},
                isOutlined = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "VIEW FULL GRAPH",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
