package com.example.personalexpensemanager.ui.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.components.ErrorScreen
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.components.appButtons.GradientIconButton
import com.example.personalexpensemanager.ui.statistics.components.PeriodFilter
import com.example.personalexpensemanager.ui.statistics.components.CategoryBarChart
import com.example.personalexpensemanager.ui.statistics.components.ExpenseLineChart
import com.example.personalexpensemanager.ui.statistics.components.Period
import com.example.personalexpensemanager.ui.statistics.components.StatisticsCard
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    onBack: () -> Unit = {}
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    StatisticsContent(
        state = state.value,
        onPeriodSelected = viewModel::onPeriodSelected,
        onRetry = viewModel::retry,
        onBack = onBack
    )
}

@Composable
fun StatisticsContent(
    state: IStatisticsUIState,
    onPeriodSelected: (Period) -> Unit = {},
    onRetry: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(top = dimensionResource(R.dimen.padding_small))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GradientIconButton(
                icon = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.action_close),
                onClick = onBack
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
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

            is IStatisticsUIState.Success -> Box(Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.statistics),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.dashboard_header_height))
                        .align(Alignment.TopCenter),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_standard))
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard)))
                    Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
                        StatisticsCard(
                            label = stringResource(R.string.statistics_spent_this_month),
                            value = formatAmount(state.currentMonthTotal),
                            highlighted = true,
                            modifier = Modifier.weight(1f)
                        )
                        StatisticsCard(
                            label = stringResource(R.string.statistics_period_total),
                            value = formatAmount(state.periodTotal),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    PeriodFilter(
                        selectedType = state.period.type,
                        onPeriodSelected = onPeriodSelected
                    )

                    ExpenseLineChart(dailySpending = state.dailySpending)

                    Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
                        StatisticsCard(
                            label = stringResource(R.string.statistics_biggest_expense),
                            value = state.biggestExpense?.amount?.let { formatAmount(it) } ?: "—",
                            highlighted = true,
                            modifier = Modifier.weight(1f)
                        )
                        StatisticsCard(
                            label = stringResource(R.string.statistics_avg_daily),
                            value = formatAmount(state.averageDaily),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    CategoryBarChart(categoryTotals = state.categoryTotals)

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard)))
                }
            }
        }
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
                        date = LocalDate.of(2026, 7, it),
                        amount = BigDecimal((20..120).random()),
                        titles = listOf("Expense $it")
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