package com.example.personalexpensemanager.ui.components.appButtons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.GradientGraphics

@Composable
fun GradientIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val brush = GradientGraphics.primaryHorizontal
    Box(
        modifier = modifier
            .size(dimensionResource(R.dimen.transaction_icon_circle_size))
            .background(Color.White, CircleShape)
            .border(
                width = dimensionResource(R.dimen.transaction_icon_border_width),
                brush = brush,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.Unspecified,
            modifier = Modifier
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(brush = brush, blendMode = BlendMode.SrcAtop)
                    }
                }
        )
    }
}