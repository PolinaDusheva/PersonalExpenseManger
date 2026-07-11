package com.example.personalexpensemanager.ui.statistics

import android.R.attr.shape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.components.ErrorScreen
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.statistics.components.PeriodFilter
import com.example.personalexpensemanager.ui.statistics.components.CategoryBarChart
import com.example.personalexpensemanager.ui.statistics.components.ExpenseLineChart
import com.example.personalexpensemanager.ui.statistics.components.Period
import com.example.personalexpensemanager.ui.statistics.components.TotalSpentCard
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate

@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    StatisticsContent(
        state = state.value,
        onPeriodSelected = viewModel::onPeriodSelected,
        onRetry = viewModel::retry
    )
}

@Composable
fun StatisticsContent(
    state: IStatisticsUIState,
    onPeriodSelected: (Period) -> Unit = {},
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(top = dimensionResource(R.dimen.padding_small))
    ) {
        Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal))) {
            Headline(text = stringResource(R.string.statistics_title))
        }

        when (state) {
            is IStatisticsUIState.Loading -> Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is IStatisticsUIState.Error -> ErrorScreen(
                messageResId = state.messageResId,
                onRetry = onRetry
            )
            is IStatisticsUIState.Success -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = dimensionResource(R.dimen.padding_horizontal),
                        end = dimensionResource(R.dimen.padding_horizontal),
                        top = 8.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_standard))
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
                    TotalSpentCard(
                        label = stringResource(R.string.statistics_spent_this_month),
                        amount = state.currentMonthTotal,
                        highlighted = true,
                        modifier = Modifier.weight(1f)
                    )
                    TotalSpentCard(
                        label = stringResource(R.string.statistics_period_total),
                        amount = state.periodTotal,
                        modifier = Modifier.weight(1f)
                    )
                }

                PeriodFilter(
                    selectedType = state.period.type,
                    onPeriodSelected = onPeriodSelected
                )

                ExpenseLineChart(dailySpending = state.dailySpending)

                Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
                    BiggestExpenseCard(
                        title = state.biggestExpense?.title,
                        amount = state.biggestExpense?.amount,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.statistics_avg_daily),
                        value = formatAmount(state.averageDaily),
                        modifier = Modifier.weight(1f)
                    )
                }

                //Headline(text = stringResource(R.string.statistics_by_category))
                CategoryBarChart(categoryTotals = state.categoryTotals)

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard)))
            }
        }
    }
}

@Composable
private fun BiggestExpenseCard(
    title: String?,
    amount: BigDecimal?,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = shape)
            .background(
                brush = Brush.linearGradient(listOf(GradientStart, GradientEnd)),
                shape = shape
            )
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.statistics_biggest_expense),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = amount?.let { formatAmount(it) } ?: "—",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = shape)
            .background(Color.White, shape)
            .padding(16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF6B7280)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
    }
}

private fun formatAmount(amount: BigDecimal): String {
    val formatter = DecimalFormat("#,##0.00")
    return "€ ${formatter.format(amount)}"
}

@Preview(showBackground = true)
@Composable
fun StatisticsPreview() {
    PersonalExpenseManagerTheme {
        StatisticsContent(
            state = IStatisticsUIState.Success(
                currentMonthTotal = BigDecimal("1240.50"),
                periodTotal = BigDecimal("312.80"),
                period = Period.thisMonth(),
                dailySpending = (1..15).map {
                    IStatisticsUIState.DailySpend(
                        LocalDate.of(2026, 7, it),
                        BigDecimal((20..120).random())
                    )
                },
                categoryTotals = listOf(
                    IStatisticsUIState.CategorySpend("Храна", BigDecimal("450")),
                    IStatisticsUIState.CategorySpend("Наем", BigDecimal("660")),
                    IStatisticsUIState.CategorySpend("Транспорт", BigDecimal("120"))
                ),
                biggestExpense = null,
                averageDaily = BigDecimal("41.35")
            )
        )
    }
}