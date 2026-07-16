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

    private fun filterExpenses(transactions: List<Transaction>): List<Transaction> {
        val expenses = mutableListOf<Transaction>()
        for (transaction in transactions) {
            if (transaction.type == TransactionType.EXPENSE) {
                expenses.add(transaction)
            }
        }
        return expenses
    }

    private fun filterByPeriod(
        expenses: List<Transaction>,
        period: Period
    ): List<Transaction> {
        val result = mutableListOf<Transaction>()
        for (expense in expenses) {
            if (!expense.date.isBefore(period.start) && !expense.date.isAfter(period.end)) {
                result.add(expense)
            }
        }
        return result
    }

    private fun calculateCurrentMonthTotal(expenses: List<Transaction>): BigDecimal {
        val now = LocalDate.now()
        var total = BigDecimal.ZERO
        for (expense in expenses) {
            if (expense.date.year == now.year && expense.date.month == now.month) {
                total += expense.amount
            }
        }
        return total
    }

    private fun calculateTotal(expenses: List<Transaction>): BigDecimal {
        var total = BigDecimal.ZERO
        for (expense in expenses) {
            total += expense.amount
        }
        return total
    }

    private fun calculateDailySpending(
        expenses: List<Transaction>,
        period: Period
    ): List<IStatisticsUIState.DailySpend> {
        val days = ChronoUnit.DAYS.between(period.start, period.end).toInt()
        val result = mutableListOf<IStatisticsUIState.DailySpend>()

        for (offset in 0..days) {
            val date = period.start.plusDays(offset.toLong())
            var dayTotal = BigDecimal.ZERO
            val dayTitles = mutableListOf<String>()
            for (expense in expenses) {
                if (expense.date == date) {
                    dayTotal += expense.amount
                    dayTitles.add(expense.title)
                }
            }
            result.add(IStatisticsUIState.DailySpend(date = date, amount = dayTotal, titles = dayTitles))
        }
        return result
    }

    private fun calculateCategoryTotals(
        expenses: List<Transaction>,
        categories: List<Category>
    ): List<IStatisticsUIState.CategorySpend> {
        val result = mutableListOf<IStatisticsUIState.CategorySpend>()

        for (category in categories) {
            var categoryTotal = BigDecimal.ZERO
            for (expense in expenses) {
                if (expense.categoryId == category.id) {
                    categoryTotal += expense.amount
                }
            }
            if (categoryTotal > BigDecimal.ZERO) {
                result.add(
                    IStatisticsUIState.CategorySpend(
                        categoryName = category.name,
                        amount = categoryTotal
                    )
                )
            }
        }
        result.sortByDescending { it.amount }
        return result
    }

    private fun findBiggestExpense(expenses: List<Transaction>): Transaction? {
        var biggest: Transaction? = null
        for (expense in expenses) {
            if (biggest == null || expense.amount > biggest.amount) {
                biggest = expense
            }
        }
        return biggest
    }

    private fun calculateAverageDaily(
        expenses: List<Transaction>,
        period: Period
    ): BigDecimal {
        val days = ChronoUnit.DAYS.between(period.start, period.end) + 1
        if (days <= 0) return BigDecimal.ZERO

        var total = BigDecimal.ZERO
        for (expense in expenses) {
            total += expense.amount
        }
        return total.divide(BigDecimal(days), 2, RoundingMode.HALF_UP)
    }
}