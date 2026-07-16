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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.statistics.IStatisticsUIState.CategorySpend
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import com.example.personalexpensemanager.ui.theme.TextPrimary
import com.example.personalexpensemanager.ui.theme.TextSecondary
import java.math.BigDecimal
import java.text.DecimalFormat
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.res.stringResource

private const val BAR_MAX_HEIGHT = 130
private val BAR_MIN_HEIGHT = 4.dp

@Composable
fun CategoryBarChart(
    categoryTotals: List<CategorySpend>,
    modifier: Modifier = Modifier
) {
    if (categoryTotals.isEmpty()) {
        val cardShape = RoundedCornerShape(dimensionResource(R.dimen.statistics_chart_corner_radius))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .shadow(elevation = dimensionResource(R.dimen.elevation), shape = cardShape, spotColor = Color.Transparent)
                .background(Color.White, cardShape)
                .padding(dimensionResource(R.dimen.statistics_chart_padding))
                .height(dimensionResource(R.dimen.bar_chart_height)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.dashboard_no_categories),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    val maxAmount = categoryTotals.maxOf { it.amount }.toFloat().coerceAtLeast(1f)
    val cardShape = RoundedCornerShape(dimensionResource(R.dimen.statistics_chart_corner_radius))
    val barBrush = Brush.verticalGradient(listOf(GradientEnd, GradientStart))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = dimensionResource(R.dimen.elevation), shape = cardShape)
            .background(Color.White, cardShape)
            .padding(dimensionResource(R.dimen.statistics_chart_padding))
            .height(dimensionResource(R.dimen.bar_chart_height)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        categoryTotals.forEach { category ->
            val fraction = category.amount.toFloat() / maxAmount
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(dimensionResource(R.dimen.bar_chart_column_width))
            ) {
                Text(
                    text = formatShort(category.amount),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.statistics_card_spacing)))
                Column(
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.bar_chart_bar_area_height))
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(dimensionResource(R.dimen.bar_chart_bar_width))
                            .height((BAR_MAX_HEIGHT * fraction).dp.coerceAtLeast(BAR_MIN_HEIGHT))
                            .background(
                                brush = barBrush,
                                shape = RoundedCornerShape(
                                    topStart = dimensionResource(R.dimen.bar_chart_bar_corner_radius),
                                    topEnd = dimensionResource(R.dimen.bar_chart_bar_corner_radius)
                                )
                            )
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                Text(
                    text = category.categoryName,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextPrimary,
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