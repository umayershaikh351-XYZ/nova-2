package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Screenshot
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassPanel
import com.example.ui.components.NovaButton
import com.example.ui.components.NovaTopBar
import com.example.ui.components.StatusDot
import com.example.ui.components.StatusDotType
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaBackgroundElevated
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPanelBorderBright
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaSecondaryAccent
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.theme.NovaTextTertiary
import com.example.viewmodel.NovaViewModel

data class DetectedObjectItem(
    val name: String,
    val icon: ImageVector,
    val confidence: String
)

val DetectedObjectsList = listOf(
    DetectedObjectItem("Laptop", Icons.Default.Computer, "99%"),
    DetectedObjectItem("Mouse", Icons.Default.Mouse, "96%"),
    DetectedObjectItem("Notebook", Icons.Default.MenuBook, "92%"),
    DetectedObjectItem("Plant", Icons.Default.LocalFlorist, "89%"),
    DetectedObjectItem("Phone", Icons.Default.PhoneAndroid, "97%")
)

@Composable
fun VisionCoreScreen(
    viewModel: NovaViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "waveTransition")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackgroundBase)
            .testTag("visioncore_screen")
    ) {
        // Top Bar
        NovaTopBar(
            title = "VisionCore",
            subtitle = "Screen & Camera Awareness",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                Box(
                    modifier = Modifier
                        .background(NovaPrimaryAccent.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StatusDot(size = 6.dp, type = StatusDotType.ACTIVE)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "LIVE",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = NovaPrimaryAccent
                        )
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 16:9 Large preview panel with scanline overlay
            GlassPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                borderColor = NovaPanelBorderBright,
                showScanlines = true
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF070B0E))
                ) {
                    // Simulated terminal / screen overlay
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "SCREEN DETECTED: WORKSTATION_01",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NovaPrimaryAccent
                            )
                            Text(
                                text = "FPS: 60 · 1080P",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = NovaSecondaryAccent
                            )
                        }

                        // Simulated code editor lines on detected screen
                        Column {
                            Text(
                                text = "01  val orchestrator = NovaNeuralCore()",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color(0x9900FF88)
                            )
                            Text(
                                text = "02  orchestrator.attachSensorTelemetry(bus)",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color(0x6600E5FF)
                            )
                            Text(
                                text = "03  while (isNominal) syncVisionStream()",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color(0x66FFFFFF)
                            )
                        }

                        // Small animated waveform bottom right
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "Bounding Boxes: 5 Objects Active",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = NovaTextTertiary
                            )

                            // Waveform Canvas
                            Canvas(modifier = Modifier.size(width = 64.dp, height = 18.dp)) {
                                val points = 12
                                val step = size.width / points
                                for (i in 0 until points) {
                                    val h = (kotlin.math.sin(wavePhase + i * 0.6) * (size.height / 2.2f) + (size.height / 2f)).toFloat()
                                    drawLine(
                                        color = NovaPrimaryAccent,
                                        start = Offset(i * step, size.height),
                                        end = Offset(i * step, size.height - h.coerceAtLeast(3f)),
                                        strokeWidth = 2.5f
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Detected Objects (5)
            Text(
                text = "DETECTED OBJECTS (5)",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NovaTextSecondary,
                letterSpacing = 1.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DetectedObjectsList.forEach { obj ->
                    GlassPanel(
                        modifier = Modifier.weight(1f),
                        borderColor = NovaPanelBorder
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = obj.icon,
                                contentDescription = obj.name,
                                tint = NovaPrimaryAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = obj.name,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 10.sp,
                                color = NovaTextPrimary,
                                maxLines = 1
                            )
                            Text(
                                text = obj.confidence,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = NovaTextTertiary
                            )
                        }
                    }
                }
            }

            // Stat Row (3 tiles)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatTile(
                    title = "TEXT RECOG",
                    value = "96%",
                    subtext = "High Fidelity",
                    accentColor = NovaPrimaryAccent,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )
                StatTile(
                    title = "OBJECTS",
                    value = "94%",
                    subtext = "YOLO v9 Deep",
                    accentColor = NovaSecondaryAccent,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )
                StatTile(
                    title = "CONFIDENCE",
                    value = "98%",
                    subtext = "System Calibration",
                    accentColor = NovaPrimaryAccent,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )
            }

            // "Current View" Card
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
                            .background(NovaPrimaryAccent.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tv,
                            contentDescription = "Desktop Screen",
                            tint = NovaPrimaryAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Desktop Screen",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NovaTextPrimary
                        )
                        Text(
                            text = "Analyzing editor buffer & documentation...",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = NovaSecondaryAccent
                        )
                    }
                }
            }
        }

        // Bottom 2 buttons: Screenshot & Camera
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NovaButton(
                onClick = {},
                isOutlined = true,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Screenshot,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "SCREENSHOT",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            NovaButton(
                onClick = {},
                isOutlined = false,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CAMERA",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
