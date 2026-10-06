package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NovaAlertDanger
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaWarning

enum class StatusDotType {
    ACTIVE,
    WAITING,
    ERROR
}

@Composable
fun StatusDot(
    modifier: Modifier = Modifier,
    type: StatusDotType = StatusDotType.ACTIVE,
    size: Dp = 10.dp
) {
    val color = when (type) {
        StatusDotType.ACTIVE -> NovaPrimaryAccent
        StatusDotType.WAITING -> NovaWarning
        StatusDotType.ERROR -> NovaAlertDanger
    }

    val transition = rememberInfiniteTransition(label = "StatusDotPulse")
    val pulseAlpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier.size(size * 1.8f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 1.8f)) {
            // Glow halo
            drawCircle(
                color = color.copy(alpha = 0.35f * pulseAlpha),
                radius = (size.toPx() / 2f) * 1.7f
            )
            // Solid center
            drawCircle(
                color = color,
                radius = size.toPx() / 2f
            )
        }
    }
}
