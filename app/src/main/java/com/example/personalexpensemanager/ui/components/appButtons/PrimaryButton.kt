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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.R
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

    val shape = RoundedCornerShape(dimensionResource(R.dimen.primary_button_corner_radius))

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
                    width = dimensionResource(R.dimen.primary_button_border_width),
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
                .padding(horizontal = dimensionResource(R.dimen.primary_button_padding_horizontal), vertical = dimensionResource(R.dimen.padding_small))
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