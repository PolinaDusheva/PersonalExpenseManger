package com.example.personalexpensemanager.ui.statistics

import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.ui.statistics.components.Period
import java.math.BigDecimal
import java.time.LocalDate


sealed interface IStatisticsUIState {
    data object Loading : IStatisticsUIState

    data class Success(
        val currentMonthTotal: BigDecimal,
        val periodTotal: BigDecimal,
        val period: Period,
        val dailySpending: List<DailySpend>,
        val categoryTotals: List<CategorySpend>,
        val biggestExpense: Transaction?,
        val averageDaily: BigDecimal
    ) : IStatisticsUIState

    data class Error(val messageResId: Int) : IStatisticsUIState

    data class DailySpend(val date: LocalDate, val amount: BigDecimal, val titles: List<String>)
    data class CategorySpend(val categoryName: String, val amount: BigDecimal)
}