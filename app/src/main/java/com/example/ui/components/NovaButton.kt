package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NovaAlertDanger
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaPrimaryAccent

@Composable
fun NovaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isOutlined: Boolean = false,
    isDanger: Boolean = false,
    enabled: Boolean = true,
    testTag: String = "nova_button",
    content: @Composable RowScope.() -> Unit
) {
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = tween(100),
        label = "btnScale"
    )

    val primaryColor = if (isDanger) NovaAlertDanger else NovaPrimaryAccent

    val handleClick = {
        if (isDanger) AudioFeedback.playAlert(view) else AudioFeedback.playClick(view)
        onClick()
    }

    if (isOutlined) {
        OutlinedButton(
            onClick = handleClick,
            modifier = modifier
                .scale(scale)
                .defaultMinSize(minHeight = 48.dp)
                .testTag(testTag),
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.2.dp, primaryColor),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = primaryColor
            ),
            interactionSource = interactionSource,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            content = content
        )
    } else {
        Button(
            onClick = handleClick,
            modifier = modifier
                .scale(scale)
                .defaultMinSize(minHeight = 48.dp)
                .testTag(testTag),
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = if (isDanger) Color.White else NovaBackgroundBase
            ),
            interactionSource = interactionSource,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            content = content
        )
    }
}
