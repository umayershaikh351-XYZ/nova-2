package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NovaCoreLogo
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaPrimaryGlow
import com.example.ui.theme.NovaTextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val BootLogLines = listOf(
    "> Initializing NOVA…",
    "> Loading core systems…",
    "> Scanning device…",
    "> Reading battery health…",
    "> Checking temperature…",
    "> Connecting agents…",
    "> Fetching live world data…",
    "> Syncing devices…",
    "> Ready."
)

@Composable
fun BootScreen(
    onBootComplete: () -> Unit
) {
    val progress = remember { Animatable(0f) }
    var displayedLineCount by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        // Animate progress smoothly over 3.8 seconds
        launch {
            progress.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(durationMillis = 3800, easing = LinearEasing)
            )
        }
        // Stream boot lines
        val lineDelay = 3800L / BootLogLines.size
        for (i in 1..BootLogLines.size) {
            delay(lineDelay)
            displayedLineCount = i
        }
        delay(400)
        onBootComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovaBackgroundBase)
            .clickable { onBootComplete() }
            .testTag("boot_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Nova Logo
            NovaCoreLogo(
                size = 120.dp,
                speedMultiplier = 1.6f
            )

            Spacer(modifier = Modifier.height(24.dp))

            // NOVA Wordmark
            Text(
                text = "NOVA",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Black,
                fontSize = 34.sp,
                letterSpacing = 6.sp,
                color = NovaPrimaryAccent,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = "NEURAL OMNI VIRTUAL AGENT",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = NovaPrimaryAccent.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Not another chatbot. An AI operating system for your life.",
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.sp,
                color = NovaTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Terminal Boot Log
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFF070B0E), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val linesToShow = BootLogLines.take(displayedLineCount).takeLast(6)
                    linesToShow.forEach { line ->
                        Text(
                            text = line,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = if (line.contains("Ready")) NovaPrimaryAccent else NovaPrimaryAccent.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress.value },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = NovaPrimaryAccent,
                trackColor = NovaPanelBorder
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Percentage
            Text(
                text = "${(progress.value * 100).toInt()}%",
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NovaPrimaryAccent
            )
        }
    }
}
