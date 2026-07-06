package com.example.personalexpensemanager.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.TransactionType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import java.time.LocalDate

class DashboardViewModel(
    private val dataService: IExpenseDataService
): ViewModel() {

    val uiState: StateFlow<IDashboardUIState> = combine(
        dataService.transactions,
        dataService.categories
    ) { transactions, categories ->
        val currentMonthTransactions = filterCurrentMonth(transactions)
        IDashboardUIState.Success(
            transactions = transactions.reversed().take(5),
            categoriesMap = calculateCategoriesPercentage(categories, currentMonthTransactions),
            totalAmount = calculateTotalExpenses(currentMonthTransactions),
            biggestExpense = calculateBiggestExpense(currentMonthTransactions)
        ) as IDashboardUIState
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IDashboardUIState.Loading
    )

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

    private fun calculateCategoriesPercentage(categories: List<Category>, transactions: List<Transaction>): HashMap<Category, Float> {
        val categoriesMap = HashMap<Category, Float>()
        val totalExpensesAmount = calculateTotalExpenses(transactions)
        for (category in categories) {
            var categoryTotalAmount: Float = 0.0f
            for (transaction in transactions) {
                if (category.id == transaction.categoryId && transaction.type == TransactionType.EXPENSE) {
                    categoryTotalAmount += transaction.amount.toFloat()
                }
            }
            categoriesMap[category] = categoryTotalAmount / totalExpensesAmount.toFloat()
        }
        return categoriesMap
    }

    fun refresh() {}
}