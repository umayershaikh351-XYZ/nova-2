package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AudioFeedback
import com.example.ui.components.NovaButton
import com.example.ui.theme.NovaAlertDanger
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.viewmodel.NovaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun KillSwitchScreen(
    viewModel: NovaViewModel,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()
    val fadeAlpha = remember { Animatable(1f) }

    val infiniteTransition = rememberInfiniteTransition(label = "killSwitchPulse")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(fadeAlpha.value)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF2E0000).copy(alpha = glowPulse),
                        Color(0xFF140000),
                        Color(0xFF000000)
                    )
                )
            )
            .testTag("kill_switch_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Large warning triangle with pulsing glow
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(NovaAlertDanger.copy(alpha = 0.15f * glowPulse), androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = NovaAlertDanger,
                    modifier = Modifier.size(72.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // KILL SWITCH Header
            Text(
                text = "KILL SWITCH",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Black,
                fontSize = 28.sp,
                letterSpacing = 4.sp,
                color = NovaAlertDanger,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Description
            Text(
                text = "This will terminate all active agents, stop automation and disconnect all device access.",
                fontFamily = FontFamily.SansSerif,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = NovaTextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "WARNING: System memory will flush and restart in safe mode.",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = NovaTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Stacked buttons: CONFIRM (solid red), CANCEL (outlined red)
            NovaButton(
                onClick = {
                    AudioFeedback.playAlert(view)
                    coroutineScope.launch {
                        fadeAlpha.animateTo(0f, animationSpec = tween(900))
                        delay(100)
                        viewModel.confirmKillSwitch()
                    }
                },
                isDanger = true,
                isOutlined = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("kill_switch_confirm")
            ) {
                Text(
                    text = "CONFIRM TERMINATION",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            NovaButton(
                onClick = {
                    AudioFeedback.playClick(view)
                    viewModel.navigateBack()
                },
                isDanger = true,
                isOutlined = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("kill_switch_cancel")
            ) {
                Text(
                    text = "CANCEL",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
