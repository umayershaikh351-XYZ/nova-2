package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaPrimaryGlow
import com.example.ui.theme.NovaSecondaryAccent
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NovaCoreLogo(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    isActive: Boolean = true,
    isCollapsing: Boolean = false,
    speedMultiplier: Float = 1f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "NovaCoreTransition")

    // Radar rotation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (8000 / speedMultiplier).toInt().coerceAtLeast(1000),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarRotation"
    )

    // Breathing glow
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    // Circuit scan
    val innerPulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "innerPulse"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(size.toPx() / 2f, size.toPx() / 2f)
            val radius = (size.toPx() / 2f) * 0.88f

            if (isCollapsing) {
                // Collapsing dot
                drawCircle(
                    color = Color.Red,
                    radius = radius * 0.1f,
                    center = center
                )
                return@Canvas
            }

            // 1. Soft radial halo glow behind
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NovaPrimaryAccent.copy(alpha = 0.28f * glowPulse),
                        NovaPrimaryGlow.copy(alpha = 0.12f * glowPulse),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius * 1.15f
                ),
                radius = radius * 1.15f,
                center = center
            )

            // 2. Concentric radar arcs (cyan)
            rotate(rotation, pivot = center) {
                // Outer arc
                drawArc(
                    color = NovaSecondaryAccent.copy(alpha = 0.75f),
                    startAngle = 15f,
                    sweepAngle = 70f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = 1.8f, cap = StrokeCap.Round)
                )
                // Opposite outer arc
                drawArc(
                    color = NovaSecondaryAccent.copy(alpha = 0.55f),
                    startAngle = 195f,
                    sweepAngle = 60f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = 1.8f, cap = StrokeCap.Round)
                )

                // Middle arc
                val midRadius = radius * 0.82f
                drawArc(
                    color = NovaSecondaryAccent.copy(alpha = 0.45f),
                    startAngle = 110f,
                    sweepAngle = 45f,
                    useCenter = false,
                    topLeft = Offset(center.x - midRadius, center.y - midRadius),
                    size = androidx.compose.ui.geometry.Size(midRadius * 2, midRadius * 2),
                    style = Stroke(width = 1.5f, cap = StrokeCap.Round)
                )

                // Sweep line
                drawLine(
                    color = NovaPrimaryAccent.copy(alpha = 0.4f * glowPulse),
                    start = center,
                    end = Offset(
                        center.x + (radius * 0.95f) * cos(Math.toRadians(0.0)).toFloat(),
                        center.y + (radius * 0.95f) * sin(Math.toRadians(0.0)).toFloat()
                    ),
                    strokeWidth = 1.5f
                )
            }

            // 3. Outer Hexagon (thin glowing green lines)
            val hexPath = Path()
            val hexRadius = radius * 0.74f
            for (i in 0 until 6) {
                val angleRad = Math.toRadians((i * 60.0) - 30.0)
                val x = center.x + hexRadius * cos(angleRad).toFloat()
                val y = center.y + hexRadius * sin(angleRad).toFloat()
                if (i == 0) hexPath.moveTo(x, y) else hexPath.lineTo(x, y)
            }
            hexPath.close()

            // Hexagon stroke
            drawPath(
                path = hexPath,
                color = NovaPrimaryAccent,
                style = Stroke(width = 2.2f)
            )

            // Outer hexagon vertices nodes
            for (i in 0 until 6) {
                val angleRad = Math.toRadians((i * 60.0) - 30.0)
                val x = center.x + hexRadius * cos(angleRad).toFloat()
                val y = center.y + hexRadius * sin(angleRad).toFloat()
                drawCircle(
                    color = NovaSecondaryAccent,
                    radius = 2.4f,
                    center = Offset(x, y)
                )
            }

            // 4. Inside: Smaller circuit-pattern hexagon
            val innerHexPath = Path()
            val innerRadius = radius * 0.44f * (if (isActive) innerPulse else 1f)
            for (i in 0 until 6) {
                val angleRad = Math.toRadians((i * 60.0) + 0.0)
                val x = center.x + innerRadius * cos(angleRad).toFloat()
                val y = center.y + innerRadius * sin(angleRad).toFloat()
                if (i == 0) innerHexPath.moveTo(x, y) else innerHexPath.lineTo(x, y)
            }
            innerHexPath.close()

            drawPath(
                path = innerHexPath,
                color = NovaPrimaryAccent.copy(alpha = 0.85f),
                style = Stroke(width = 1.5f)
            )

            // Inner circuit traces
            for (i in 0 until 6) {
                val a1 = Math.toRadians((i * 60.0) + 0.0)
                val a2 = Math.toRadians((i * 60.0) - 30.0)
                val p1 = Offset(center.x + innerRadius * cos(a1).toFloat(), center.y + innerRadius * sin(a1).toFloat())
                val p2 = Offset(center.x + hexRadius * 0.85f * cos(a2).toFloat(), center.y + hexRadius * 0.85f * sin(a2).toFloat())
                drawLine(
                    color = NovaPrimaryAccent.copy(alpha = 0.35f),
                    start = p1,
                    end = p2,
                    strokeWidth = 1.0f
                )
            }

            // Center core nucleus
            drawCircle(
                color = NovaPrimaryAccent,
                radius = radius * 0.12f,
                center = center
            )
            drawCircle(
                color = Color.White,
                radius = radius * 0.05f,
                center = center
            )
        }
    }
}
