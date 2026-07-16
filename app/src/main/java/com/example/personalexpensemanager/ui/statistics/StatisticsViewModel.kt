package com.example.personalexpensemanager.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.TransactionHelper
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
            dataService.categories,
            _selectedPeriod
        ) { transactions, categories, period ->
            val allExpenses = filterExpenses(transactions)
            val periodExpenses = filterByPeriod(allExpenses, period)

            IStatisticsUIState.Success(
                currentMonthTotal = TransactionHelper.calculateTotalExpenses(
                    TransactionHelper.filterCurrentMonth(transactions)
                ),
                periodTotal = calculateTotal(periodExpenses),
                period = period,
                dailySpending = calculateDailySpending(periodExpenses, period),
                categoryTotals = calculateCategoryTotals(periodExpenses, categories),
                biggestExpense = findBiggestExpense(periodExpenses),
                averageDaily = calculateAverageDaily(periodExpenses, period)
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

    private fun filterExpenses(transactions: List<Transaction>): List<Transaction> =
        transactions.filter { it.type == TransactionType.EXPENSE }

    private fun filterByPeriod(
        expenses: List<Transaction>,
        period: Period
    ): List<Transaction> =
        expenses.filter { !it.date.isBefore(period.start) && !it.date.isAfter(period.end) }

    private fun calculateTotal(expenses: List<Transaction>): BigDecimal =
        expenses.fold(BigDecimal.ZERO) { acc, e -> acc + e.amount }

    private fun calculateDailySpending(
        expenses: List<Transaction>,
        period: Period
    ): List<IStatisticsUIState.DailySpend> {
        val days = ChronoUnit.DAYS.between(period.start, period.end).toInt()
        return (0..days).map { offset ->
            val date = period.start.plusDays(offset.toLong())
            val expensesForDay = expenses.filter { it.date == date }
            IStatisticsUIState.DailySpend(
                date = date,
                amount = expensesForDay.fold(BigDecimal.ZERO) { acc, e -> acc + e.amount },
                titles = expensesForDay.map { it.title }
            )
        }
    }

    private fun calculateCategoryTotals(
        expenses: List<Transaction>,
        categories: List<Category>
    ): List<IStatisticsUIState.CategorySpend> =
        categories
            .map { category ->
                val total = expenses
                    .filter { it.categoryId == category.id }
                    .fold(BigDecimal.ZERO) { acc, e -> acc + e.amount }
                IStatisticsUIState.CategorySpend(categoryName = category.name, amount = total)
            }
            .filter { it.amount > BigDecimal.ZERO }
            .sortedByDescending { it.amount }

    private fun findBiggestExpense(expenses: List<Transaction>): Transaction? =
        expenses.maxByOrNull { it.amount }

    private fun calculateAverageDaily(
        expenses: List<Transaction>,
        period: Period
    ): BigDecimal {
        val days = ChronoUnit.DAYS.between(period.start, period.end) + 1
        if (days <= 0) return BigDecimal.ZERO

        val total = expenses.fold(BigDecimal.ZERO) { acc, e -> acc + e.amount }
        return total.divide(BigDecimal(days), 2, RoundingMode.HALF_UP)
    }
}