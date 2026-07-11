package com.example.personalexpensemanager.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.TransactionType
import com.example.personalexpensemanager.ui.statistics.components.Period
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class StatisticsViewModel(
    private val dataService: IExpenseDataService
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(Period.thisMonth())
    val selectedPeriod: StateFlow<Period> = _selectedPeriod

    private val retrySignal = MutableStateFlow(0)

    val uiState: StateFlow<IStatisticsUIState> = retrySignal.flatMapLatest {
        combine(
            dataService.transactions,
            _selectedPeriod
        ) { transactions, period ->
            val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
            val periodExpenses = expenses.filter {
                !it.date.isBefore(period.start) && !it.date.isAfter(period.end)
            }
            IStatisticsUIState.Success(
                currentMonthTotal = currentMonthTotal(expenses),
                periodTotal = periodExpenses.sumOf { it.amount },
                period = period,
                dailySpending = dailySpending(periodExpenses, period),
                categoryTotals = categoryTotals(periodExpenses),
                biggestExpense = periodExpenses.maxByOrNull { it.amount },
                averageDaily = averageDaily(periodExpenses, period)
            ) as IStatisticsUIState
        }.catch { e ->
            emit(IStatisticsUIState.Error(R.string.error_load_statistics))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IStatisticsUIState.Loading
    )

    fun retry() { retrySignal.value++ }

    fun onPeriodSelected(period: Period) {
        _selectedPeriod.value = period
    }

    private fun currentMonthTotal(expenses: List<Transaction>): BigDecimal {
        val now = LocalDate.now()
        return expenses
            .filter { it.date.year == now.year && it.date.month == now.month }
            .sumOf { it.amount }
    }

    private fun dailySpending(
        expenses: List<Transaction>,
        period: Period
    ): List<IStatisticsUIState.DailySpend> {
        val byDate = expenses.groupBy { it.date }
        val days = ChronoUnit.DAYS.between(period.start, period.end).toInt()
        return (0..days).map { offset ->
            val date = period.start.plusDays(offset.toLong())
            IStatisticsUIState.DailySpend(
                date = date,
                amount = byDate[date]?.sumOf { it.amount } ?: BigDecimal.ZERO
            )
        }
    }

    private fun categoryTotals(
        expenses: List<Transaction>
    ): List<IStatisticsUIState.CategorySpend> {
        val categories = dataService.categories.value
        return expenses
            .groupBy { it.categoryId }
            .map { (categoryId, list) ->
                IStatisticsUIState.CategorySpend(
                    categoryName = categories.find { it.id == categoryId }?.name ?: "?",
                    amount = list.sumOf { it.amount }
                )
            }
            .sortedByDescending { it.amount }
    }

    private fun averageDaily(expenses: List<Transaction>, period: Period): BigDecimal {
        val days = ChronoUnit.DAYS.between(period.start, period.end) + 1
        if (days <= 0) return BigDecimal.ZERO
        return expenses.sumOf { it.amount }
            .divide(BigDecimal(days), 2, RoundingMode.HALF_UP)
    }
}