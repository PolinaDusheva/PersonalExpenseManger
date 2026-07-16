package com.example.personalexpensemanager.ui.goals.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun MonthlyExpenseArc(
    totalSpent: BigDecimal,
    budget: BigDecimal?,
    label: String,
    modifier: Modifier = Modifier
) {
    val trackColor = colorResource(R.color.arc_track)
    val grayTrack = colorResource(R.color.arc_track_gray)
    val labelColor = colorResource(R.color.arc_label)
    val amountColor = colorResource(R.color.arc_amount)
    val strokeWidthDp = dimensionResource(R.dimen.arc_stroke_width)
    val hasBudget = budget != null && budget > BigDecimal.ZERO

    val progress = if (hasBudget) {
        totalSpent.divide(budget, 4, RoundingMode.HALF_UP)
            .toFloat().coerceIn(0f, 1f)
    } else 0f

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.4f)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(1f)
        ) {
            val strokeWidth = strokeWidthDp.toPx()
            val padding = strokeWidth / 2
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(padding, padding)

            val startAngle = 150f
            val maxSweep = 240f

            drawArc(
                color = if (hasBudget) trackColor else grayTrack,
                startAngle = startAngle,
                sweepAngle = maxSweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            if (hasBudget) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            GradientStart.copy(alpha = 0.4f),
                            GradientStart,
                            GradientEnd,
                            GradientEnd.copy(alpha = 0.4f)
                        )
                    ),
                    startAngle = startAngle,
                    sweepAngle = maxSweep * progress,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = labelColor,
                letterSpacing = dimensionResource(R.dimen.arc_label_letter_spacing).value.sp
            )
            Text(
                text = formatAmount(totalSpent),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
            if (hasBudget) {
                Text(
                    text = "/ ${formatAmount(budget!!)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = labelColor
                )
            }
        }
    }
}

private fun formatAmount(amount: BigDecimal): String {
    val formatter = DecimalFormat("#,##0.00")
    return "€${formatter.format(amount)}"
}

@Preview(showBackground = true)
@Composable
fun SpentGaugeArcWithBudgetPreview() {
    PersonalExpenseManagerTheme {
        MonthlyExpenseArc(
            totalSpent = BigDecimal("620.00"),
            budget = BigDecimal("1500.00"),
            label = "SPENT OF BUDGET"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SpentGaugeArcNoBudgetPreview() {
    PersonalExpenseManagerTheme {
        MonthlyExpenseArc(
            totalSpent = BigDecimal.ZERO,
            budget = null,
            label = "SPENT OF BUDGET"
        )
    }
}