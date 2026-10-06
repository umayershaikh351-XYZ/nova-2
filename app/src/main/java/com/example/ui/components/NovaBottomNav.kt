package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.NovaNavTarget
import com.example.ui.theme.NovaBackgroundElevated
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaTextTertiary

data class BottomNavItem(
    val target: NovaNavTarget,
    val title: String,
    val icon: ImageVector,
    val testTag: String
)

val BottomNavItems = listOf(
    BottomNavItem(NovaNavTarget.DASHBOARD, "Home", Icons.Default.Home, "nav_home"),
    BottomNavItem(NovaNavTarget.AGENT_PANEL, "Agents", Icons.Default.SmartToy, "nav_agents"),
    BottomNavItem(NovaNavTarget.COMMAND_LINE, "Command", Icons.Default.Terminal, "nav_command"),
    BottomNavItem(NovaNavTarget.DEVICE_SCAN, "System", Icons.Default.Person, "nav_system")
)

@Composable
fun NovaBottomNav(
    currentTarget: NovaNavTarget,
    onSelectTarget: (NovaNavTarget) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NovaBackgroundElevated)
    ) {
        HorizontalDivider(thickness = 1.dp, color = NovaPanelBorder)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItems.forEach { item ->
                val isSelected = currentTarget == item.target

                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                AudioFeedback.playClick(view)
                                onSelectTarget(item.target)
                            }
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag(item.testTag)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                color = if (isSelected) NovaPrimaryAccent.copy(alpha = 0.15f) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) NovaPrimaryAccent else NovaTextTertiary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Text(
                        text = item.title.uppercase(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) NovaPrimaryAccent else NovaTextTertiary,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
