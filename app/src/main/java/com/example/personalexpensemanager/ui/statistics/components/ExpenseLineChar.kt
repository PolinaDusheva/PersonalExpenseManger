package com.example.personalexpensemanager.ui.statistics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personalexpensemanager.ui.statistics.IStatisticsUIState.DailySpend
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun ExpenseLineChart(
    dailySpending: List<DailySpend>,
    modifier: Modifier = Modifier
) {
    if (dailySpending.isEmpty()) return

    var selectedIndex by remember(dailySpending) {
        mutableIntStateOf(dailySpending.lastIndex)
    }
    val selected = dailySpending[selectedIndex]
    val cardShape = RoundedCornerShape(24.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = cardShape)
            .background(Color.White, cardShape)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected.date.format(DateTimeFormatter.ofPattern("d MMM")),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B7280)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = formatAmount(selected.amount),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .pointerInput(dailySpending) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            selectedIndex = nearestIndex(offset.x, size.width, dailySpending.size)
                        },
                        onDrag = { change, _ ->
                            selectedIndex = nearestIndex(change.position.x, size.width, dailySpending.size)
                        }
                    )
                }
        ) {
            val stepX = if (dailySpending.size > 1) {
                size.width / (dailySpending.size - 1)
            } else size.width

            val maxAmount = dailySpending
                .maxOf { it.amount }
                .toFloat()
                .coerceAtLeast(1f)

            val points = dailySpending.mapIndexed { i, day ->
                Offset(
                    x = if (dailySpending.size > 1) i * stepX else size.width / 2,
                    y = size.height - (day.amount.toFloat() / maxAmount) * size.height * 0.85f
                )
            }

            points.forEach { point ->
                drawLine(
                    color = Color(0xFFE3DEF0),
                    start = Offset(point.x, 0f),
                    end = Offset(point.x, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }

            val path = Path()
            points.forEachIndexed { i, point ->
                if (i == 0) {
                    path.moveTo(point.x, point.y)
                } else {
                    val prev = points[i - 1]
                    val midX = (prev.x + point.x) / 2
                    path.cubicTo(midX, prev.y, midX, point.y, point.x, point.y)
                }
            }
            drawPath(
                path = path,
                brush = Brush.horizontalGradient(listOf(GradientStart, GradientEnd)),
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

            val selectedPoint = points[selectedIndex]
            drawCircle(
                color = GradientStart.copy(alpha = 0.25f),
                radius = 18.dp.toPx(),
                center = selectedPoint
            )
            drawCircle(
                brush = Brush.linearGradient(listOf(GradientStart, GradientEnd)),
                radius = 8.dp.toPx(),
                center = selectedPoint
            )
        }
    }
}

private fun nearestIndex(x: Float, width: Int, count: Int): Int {
    if (count <= 1) return 0
    val step = width.toFloat() / (count - 1)
    return (x / step).roundToInt().coerceIn(0, count - 1)
}

private fun formatAmount(amount: BigDecimal): String {
    val formatter = DecimalFormat("#,##0.00")
    return "€ ${formatter.format(amount)}"
}

@Preview(showBackground = true)
@Composable
fun ExpenseLineChartPreview() {
    PersonalExpenseManagerTheme {
        ExpenseLineChart(
            dailySpending = listOf(
                DailySpend(LocalDate.of(2026, 7, 1), BigDecimal("20")),
                DailySpend(LocalDate.of(2026, 7, 2), BigDecimal("45")),
                DailySpend(LocalDate.of(2026, 7, 3), BigDecimal("30")),
                DailySpend(LocalDate.of(2026, 7, 4), BigDecimal("80")),
                DailySpend(LocalDate.of(2026, 7, 5), BigDecimal("60")),
                DailySpend(LocalDate.of(2026, 7, 6), BigDecimal("95")),
                DailySpend(LocalDate.of(2026, 7, 7), BigDecimal("40"))
            )
        )
    }
}