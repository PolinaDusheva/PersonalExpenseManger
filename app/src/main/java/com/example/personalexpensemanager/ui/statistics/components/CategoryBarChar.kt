package com.example.personalexpensemanager.ui.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensemanager.ui.statistics.IStatisticsUIState.CategorySpend
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.math.BigDecimal
import java.text.DecimalFormat

@Composable
fun CategoryBarChart(
    categoryTotals: List<CategorySpend>,
    modifier: Modifier = Modifier
) {
    if (categoryTotals.isEmpty()) return

    val maxAmount = categoryTotals.maxOf { it.amount }.toFloat().coerceAtLeast(1f)
    val cardShape = RoundedCornerShape(24.dp)
    val barBrush = Brush.verticalGradient(listOf(GradientEnd, GradientStart))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = cardShape)
            .background(Color.White, cardShape)
            .padding(20.dp)
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        categoryTotals.forEach { category ->
            val fraction = category.amount.toFloat() / maxAmount
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(56.dp)
            ) {
                Text(
                    text = formatShort(category.amount),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF6B7280)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Column(
                    modifier = Modifier
                        .height(130.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(28.dp)
                            .height((130 * fraction).dp.coerceAtLeast(4.dp))
                            .background(
                                brush = barBrush,
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = category.categoryName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = Color(0xFF1F2937),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun formatShort(amount: BigDecimal): String =
    DecimalFormat("#,##0").format(amount)

@Preview(showBackground = true)
@Composable
fun CategoryBarChartPreview() {
    PersonalExpenseManagerTheme {
        CategoryBarChart(
            categoryTotals = listOf(
                CategorySpend("Храна", BigDecimal("450")),
                CategorySpend("Наем", BigDecimal("660")),
                CategorySpend("Транспорт", BigDecimal("120")),
                CategorySpend("Забавления", BigDecimal("210"))
            )
        )
    }
}