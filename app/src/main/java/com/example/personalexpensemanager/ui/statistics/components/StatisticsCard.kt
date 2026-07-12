package com.example.personalexpensemanager.ui.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientStart

@Composable
fun StatisticsCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    highlighted: Boolean = false
) {
    val shape = RoundedCornerShape(dimensionResource(R.dimen.statistics_card_corner_radius))
    val background = if (highlighted) {
        Modifier.background(
            brush = Brush.linearGradient(listOf(GradientStart, GradientEnd)),
            shape = shape
        )
    } else {
        Modifier.background(Color.White, shape)
    }
    val labelColor = if (highlighted) Color.White.copy(alpha = 0.8f) else Color(0xFF6B7280)
    val valueColor = if (highlighted) Color.White else Color(0xFF1F2937)
    val subtitleColor = if (highlighted) Color.White.copy(alpha = 0.8f) else Color(0xFF6B7280)

    Column(
        modifier = modifier
            .shadow(elevation = dimensionResource(R.dimen.statistics_card_elevation), shape = shape)
            .then(background)
            .padding(dimensionResource(R.dimen.padding_standard))
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = labelColor,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.statistics_card_spacing)))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = subtitleColor
            )
        }
    }
}