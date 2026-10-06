package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary

@Composable
fun NovaTopBar(
    title: String? = null,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null,
    onNotificationsClick: (() -> Unit)? = null,
    onRefreshClick: (() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    val view = LocalView.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            if (onBackClick != null) {
                IconButton(
                    onClick = {
                        AudioFeedback.playClick(view)
                        onBackClick()
                    },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = NovaPrimaryAccent
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
            } else {
                // Main logo header
                NovaCoreLogo(
                    size = 28.dp,
                    speedMultiplier = 1.2f
                )
                Spacer(modifier = Modifier.width(10.dp))
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title ?: "NOVA",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        letterSpacing = 1.2.sp,
                        color = NovaTextPrimary
                    )
                    if (onBackClick == null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        StatusDot(size = 7.dp, type = StatusDotType.ACTIVE)
                    }
                }
                Text(
                    text = subtitle ?: (if (onBackClick == null) "SYSTEM ONLINE" else ""),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    letterSpacing = 0.8.sp,
                    color = if (onBackClick == null) NovaPrimaryAccent else NovaTextSecondary
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (actions != null) {
                actions()
            } else {
                if (onRefreshClick != null) {
                    IconButton(
                        onClick = {
                            AudioFeedback.playClick(view)
                            onRefreshClick()
                        },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Telemetry",
                            tint = NovaPrimaryAccent
                        )
                    }
                }
                if (onSearchClick != null) {
                    IconButton(
                        onClick = {
                            AudioFeedback.playClick(view)
                            onSearchClick()
                        },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Systems",
                            tint = NovaTextSecondary
                        )
                    }
                }
                if (onNotificationsClick != null) {
                    IconButton(
                        onClick = {
                            AudioFeedback.playClick(view)
                            onNotificationsClick()
                        },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Alerts & Notifications",
                            tint = NovaPrimaryAccent
                        )
                    }
                }
            }
        }
    }
}
