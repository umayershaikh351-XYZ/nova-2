package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NovaAlertDanger
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaWarning

@Composable
fun GaugeRing(
    modifier: Modifier = Modifier,
    percentage: Float, // 0f to 100f
    size: Dp = 80.dp,
    strokeWidth: Dp = 7.dp,
    customColor: Color? = null,
    backgroundColor: Color = Color(0x221A2A22),
    centerContent: (@Composable () -> Unit)? = null
) {
    val animatedPercent by animateFloatAsState(
        targetValue = percentage.coerceIn(0f, 100f),
        animationSpec = tween(durationMillis = 600),
        label = "gaugeAnimation"
    )

    val activeColor = customColor ?: when {
        animatedPercent >= 90f -> NovaAlertDanger
        animatedPercent >= 80f -> NovaWarning
        else -> NovaPrimaryAccent
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val radius = (size.toPx() - strokePx) / 2f
            val topLeft = Offset(strokePx / 2f, strokePx / 2f)
            val canvasSize = Size(radius * 2, radius * 2)

            // Background track
            drawArc(
                color = backgroundColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = canvasSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Active progress arc
            val sweep = (animatedPercent / 100f) * 360f
            if (sweep > 0f) {
                // Glow
                drawArc(
                    color = activeColor.copy(alpha = 0.35f),
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = canvasSize,
                    style = Stroke(width = strokePx * 1.6f, cap = StrokeCap.Round)
                )

                // Foreground arc
                drawArc(
                    color = activeColor,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = canvasSize,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )
            }
        }

        centerContent?.invoke()
    }
}
