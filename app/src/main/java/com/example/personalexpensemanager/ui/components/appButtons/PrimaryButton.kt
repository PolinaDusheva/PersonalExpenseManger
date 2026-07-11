package com.example.personalexpensemanager.ui.components.appButtons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientGraphics
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isActive = isPressed || isHovered

    val backgroundAlpha by animateFloatAsState(
        if (isActive) 0f else 1f, label = "bgAlpha"
    )
    val textColor by animateColorAsState(
        if (isActive) GradientStart else Color.White, label = "textColor"
    )

    val shape = RoundedCornerShape(70.dp)

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(
                    width = 2.dp,
                    brush = GradientGraphics.primaryHorizontal,
                    shape = shape
                )
                .background(Color.White, shape)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            GradientStart.copy(alpha = backgroundAlpha),
                            GradientEnd.copy(alpha = backgroundAlpha)
                        )
                    ),
                    shape = shape
                )
                .clip(shape)
                .padding(horizontal = 70.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryButtonPreview() {
    PersonalExpenseManagerTheme {
        PrimaryButton(
            text = "Next", 
            onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryButtonDisabledPreview() {
    PersonalExpenseManagerTheme {
        PrimaryButton(
            text = "Next",
            onClick = {}, 
            enabled = false)
    }
}