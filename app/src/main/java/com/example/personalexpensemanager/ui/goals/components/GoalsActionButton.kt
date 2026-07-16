package com.example.personalexpensemanager.ui.goals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.GradientGraphics

@Composable
fun GoalActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(dimensionResource(R.dimen.statistics_card_corner_radius))
    val iconBrush = GradientGraphics.primaryHorizontal

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2.2f)
                .shadow(elevation = dimensionResource(R.dimen.elevation), shape = shape)
                .clip(shape)
                .background(Color.White, shape)
                .clickable { onClick() }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.category_card_icon_size))
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush = iconBrush, blendMode = BlendMode.SrcAtop)
                        }
                    }
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}