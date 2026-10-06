package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassPanel
import com.example.ui.components.NovaTopBar
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaBackgroundElevated
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaSecondaryAccent
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.theme.NovaTextTertiary
import com.example.viewmodel.NovaViewModel

@Composable
fun CommandLineScreen(
    viewModel: NovaViewModel,
    modifier: Modifier = Modifier
) {
    val inputCommand by viewModel.currentCommandInput.collectAsState()
    val isProcessing by viewModel.isCommandProcessing.collectAsState()
    val progressValue by viewModel.commandProgress.collectAsState()
    val liveOutputs by viewModel.liveCommandOutputs.collectAsState()
    val routedAgents by viewModel.routedAgents.collectAsState()

    val animatedProgress by animateFloatAsState(
        targetValue = progressValue,
        animationSpec = tween(400),
        label = "cmdProg"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackgroundBase)
            .testTag("commandline_screen")
    ) {
        // Top Bar
        NovaTopBar(
            title = "CommandLine",
            subtitle = "Direct control. Natural language.",
            onBackClick = { viewModel.navigateBack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Prompt Card
            item {
                Spacer(modifier = Modifier.height(4.dp))
                GlassPanel(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NovaPrimaryAccent.copy(alpha = 0.5f),
                    showScanlines = true
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "TERMINAL PROMPT",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = NovaTextTertiary,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "NOVA:// ",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = NovaSecondaryAccent
                            )
                            Text(
                                text = if (inputCommand.isNotBlank()) inputCommand else "optimize sensor telemetry & scan devices",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = NovaPrimaryAccent
                            )
                        }
                    }
                }
            }

            // Routing to agents...
            item {
                Text(
                    text = "ROUTING TO AGENTS...",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovaTextSecondary,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        routedAgents.forEach { agentName ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Active",
                                        tint = NovaPrimaryAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = agentName,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = NovaTextPrimary
                                    )
                                }
                                Text(
                                    text = "CONNECTED",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = NovaPrimaryAccent
                                )
                            }
                        }
                    }
                }
            }

            // Processing Progress
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isProcessing) "PROCESSING..." else "EXECUTION COMPLETE",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NovaPrimaryAccent,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NovaPrimaryAccent
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = NovaPrimaryAccent,
                    trackColor = NovaPanelBorder
                )
            }

            // Live Output
            item {
                Text(
                    text = "LIVE OUTPUT",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovaTextSecondary,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                GlassPanel(
                    modifier = Modifier.fillMaxWidth(),
                    showScanlines = true
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        liveOutputs.forEach { line ->
                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "• ",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = NovaPrimaryAccent
                                )
                                Text(
                                    text = line,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    color = NovaTextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Quick suggestion chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val quickCommands = listOf("scan devices", "status all", "crypto summary")
                    quickCommands.forEach { qc ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NovaBackgroundElevated)
                                .clickable {
                                    viewModel.executeCommand(qc)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = qc,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = NovaSecondaryAccent
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Bottom Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NovaBackgroundElevated)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputCommand,
                onValueChange = { viewModel.setCommandInput(it) },
                placeholder = {
                    Text(
                        text = "Type a command…",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = NovaTextTertiary
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("command_input"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NovaPrimaryAccent,
                    unfocusedBorderColor = NovaPanelBorder,
                    focusedTextColor = NovaTextPrimary,
                    unfocusedTextColor = NovaTextPrimary,
                    cursorColor = NovaPrimaryAccent
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (inputCommand.isNotBlank()) viewModel.executeCommand(inputCommand)
                })
            )

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(
                onClick = {
                    if (inputCommand.isNotBlank()) {
                        viewModel.executeCommand(inputCommand)
                    } else {
                        viewModel.executeCommand("scan devices")
                    }
                },
                modifier = Modifier
                    .size(46.dp)
                    .background(NovaPrimaryAccent, CircleShape)
                    .testTag("send_command_button")
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = NovaBackgroundBase
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Command",
                        tint = NovaBackgroundBase,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
