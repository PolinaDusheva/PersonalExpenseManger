package com.example.personalexpensemanager.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import java.time.LocalDate

class DashboardViewModel(
    private val dataService: IExpenseDataService
) : ViewModel() {

    private val retrySignal = MutableStateFlow(0)

    val uiState: StateFlow<IDashboardUIState> = retrySignal.flatMapLatest {
        combine(
            dataService.transactions,
            dataService.categories
        ) { transactions, categories ->
            if (transactions.isEmpty() && categories.isEmpty()) {
                IDashboardUIState.Empty
            } else {
                val currentMonthTransactions = filterCurrentMonth(transactions)
                IDashboardUIState.Success(
                    transactions = transactions.reversed().take(5),
                    categoriesMap = calculateCategoriesPercentage(categories, currentMonthTransactions),
                    totalAmount = calculateTotalExpenses(currentMonthTransactions),
                    biggestExpense = calculateBiggestExpense(currentMonthTransactions)
                )
            }
        }.catch { e ->
            emit(IDashboardUIState.Error(R.string.error_load_dashboard))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IDashboardUIState.Loading
    )

    fun retry() { retrySignal.value++ }


    private fun filterCurrentMonth(transactions: List<Transaction>): List<Transaction> {
        val now = LocalDate.now()
        return transactions.filter { it.date.year == now.year && it.date.month == now.month }
    }

    private fun calculateTotalExpenses(transactions: List<Transaction>): BigDecimal {
        var amount: BigDecimal = BigDecimal.ZERO
        for (currentTransaction in transactions) {
            if (currentTransaction.type == TransactionType.EXPENSE) {
                amount += currentTransaction.amount
            }
        }
        return amount
    }

    private fun calculateBiggestExpense(transactions: List<Transaction>): BigDecimal {
        var maxExpense: BigDecimal = BigDecimal.ZERO
        for (currentTransaction in transactions) {
            val currAmount = currentTransaction.amount
            if (currentTransaction.type == TransactionType.EXPENSE && currAmount > maxExpense) {
                maxExpense = currAmount
            }
        }
        return maxExpense
    }

    private fun calculateCategoriesPercentage(
        categories: List<Category>,
        transactions: List<Transaction>
    ): HashMap<Category, Float> {
        val categoriesMap = HashMap<Category, Float>()
        val totalExpensesAmount = calculateTotalExpenses(transactions)
        for (category in categories) {
            var categoryTotalAmount: Float = 0.0f
            for (transaction in transactions) {
                if (category.id == transaction.categoryId && transaction.type == TransactionType.EXPENSE) {
                    categoryTotalAmount += transaction.amount.toFloat()
                }
            }
            categoriesMap[category] = if (totalExpensesAmount > BigDecimal.ZERO) {
                categoryTotalAmount / totalExpensesAmount.toFloat()
            } else {
                0f
            }
        }
        return categoriesMap
    }

    fun refresh() {}
}