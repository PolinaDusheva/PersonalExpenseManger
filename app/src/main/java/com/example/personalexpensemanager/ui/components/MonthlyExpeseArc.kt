package com.example.personalexpensemanager.ui.statistics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.math.BigDecimal
import java.text.DecimalFormat

@Composable
fun MonthlyExpenseArc(
    totalSpent: BigDecimal,
    label: String,
    modifier: Modifier = Modifier
) {
    val trackColor = Color(0xFFE8E0F0)

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
            val strokeWidth = 18.dp.toPx()
            val padding = strokeWidth / 2
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(padding, padding)

            val startAngle = 150f
            val sweepAngle = 240f

            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

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
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B7280),
                letterSpacing = 2.sp
            )
            Text(
                text = formatAmount(totalSpent),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
        }
    }
}

private fun formatAmount(amount: BigDecimal): String {
    val formatter = DecimalFormat("#,##0.00")
    return "€ ${formatter.format(amount)}"
}

@Preview(showBackground = true)
@Composable
fun SpentGaugeArcPreview() {
    PersonalExpenseManagerTheme {
        MonthlyExpenseArc(
            totalSpent = BigDecimal("6210.18"),
            label = "РАЗХОДИ ТОЗИ МЕСЕЦ"
        )
    }
}