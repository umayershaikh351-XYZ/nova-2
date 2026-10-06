package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NovaDarkColorScheme = darkColorScheme(
    primary = NovaPrimaryAccent,
    onPrimary = NovaBackgroundBase,
    primaryContainer = NovaBackgroundElevated,
    onPrimaryContainer = NovaPrimaryAccent,
    secondary = NovaSecondaryAccent,
    onSecondary = NovaBackgroundBase,
    secondaryContainer = NovaBackgroundElevated,
    onSecondaryContainer = NovaSecondaryAccent,
    tertiary = NovaWarning,
    background = NovaBackgroundBase,
    onBackground = NovaTextPrimary,
    surface = NovaPanelSurface,
    onSurface = NovaTextPrimary,
    surfaceVariant = NovaBackgroundElevated,
    onSurfaceVariant = NovaTextSecondary,
    outline = NovaPanelBorder,
    error = NovaAlertDanger,
    onError = NovaTextPrimary
)

@Composable
fun NovaTheme(
    content: @Composable () -> Unit
) {
    // NOVA is strictly dark mode only
    MaterialTheme(
        colorScheme = NovaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
