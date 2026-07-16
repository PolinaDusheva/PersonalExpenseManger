package com.example.personalexpensemanager.ui.goals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.goals.IGoalsUIState
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientGraphics
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.TextSecondary
import com.example.personalexpensemanager.ui.theme.ProgressTrackLight
import java.math.BigDecimal
import java.text.DecimalFormat

@Composable
fun GoalProgressItem(
    goalProgress: IGoalsUIState.GoalProgress,
    onClick: () -> Unit = {}
) {
    val iconBrush = GradientGraphics.primaryHorizontal
    val fraction = if (goalProgress.goal.targetAmount > BigDecimal.ZERO) {
        (goalProgress.currentAmount.toFloat() / goalProgress.goal.targetAmount.toFloat())
            .coerceIn(0f, 1f)
    } else 0f

    Column(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.padding_small)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.category_row_icon_spacing)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Radar,
                contentDescription = goalProgress.goal.title,
                tint = Color.Unspecified,
                modifier = Modifier
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush = iconBrush, blendMode = BlendMode.SrcAtop)
                        }
                    }
            )
            Column {
                Text(text = goalProgress.goal.title)
                Text(
                    text = "${formatAmount(goalProgress.currentAmount)} / ${formatAmount(goalProgress.goal.targetAmount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "${(fraction * 100).toInt()}%")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.category_progress_height))
                .padding(bottom = dimensionResource(R.dimen.padding_small))
                .background(ProgressTrackLight, RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.horizontalGradient(listOf(GradientStart, GradientEnd)),
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    }
}

private fun formatAmount(amount: BigDecimal): String {
    val formatter = DecimalFormat("#,##0.00")
    return "€ ${formatter.format(amount)}"
}