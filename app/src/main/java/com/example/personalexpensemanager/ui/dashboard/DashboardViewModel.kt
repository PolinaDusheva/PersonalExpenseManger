package com.example.personalexpensemanager.ui.dashboard

import androidx.compose.animation.core.Transition
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.ExpenseDataService
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dataService: ExpenseDataService
): ViewModel() {

    val uiState: StateFlow<DashboardUIState> = combine(
        dataService.transactions,
        dataService.categories
    ) { transactions, categories ->
        DashboardUIState.Success(
            transactions = transactions.reversed().take(5),
            categoriesMap = calculateCategoriesPercentage(categories, transactions),
            totalAmount = calculateTotalAmount(transactions),
            biggestExpense = calculateBiggestExpense(transactions)
        ) as DashboardUIState
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUIState.Loading
    )
    private fun calculateTotalAmount(transactions: List<Transaction>): Double {
        var amount: Double = 0.0
        for(currentTransaction in transactions) {
            var currAmount = currentTransaction.amount
            if (currentTransaction.type == TransactionType.EXPENSE) {
                currAmount *= -1
            }
            amount += currAmount
        }
        return amount
    }
    private fun calculateTotalExpenses(transactions: List<Transaction>): Double{
        var amount: Double = 0.0
        for(currentTransaction in transactions) {
            var currAmount = currentTransaction.amount
            if (currentTransaction.type == TransactionType.EXPENSE) {
                amount += currAmount;
            }
        }
        return amount
    }
    private fun calculateBiggestExpense(transactions: List<Transaction>): Double {
        var maxExpense: Double = 0.0
        for(currentTransaction in transactions){
            var currAmount = currentTransaction.amount
            if (currentTransaction.type == TransactionType.EXPENSE && currAmount > maxExpense) {
                maxExpense = currAmount
            }
        }
        return maxExpense

    }

    private fun calculateCategoriesPercentage(categories: List<Category>, transactions:List<Transaction>): HashMap<Category, Float>{
        val categoriesMap = HashMap<Category, Float>()
        val totalExpensesAmount = calculateTotalExpenses(transactions)
        for (category in categories){
            var categoryTotalAmount: Float = 0.0f
            for (transaction in transactions) {
                if (category.id ==transaction.categoryId && transaction.type == TransactionType.EXPENSE){
                    categoryTotalAmount+=transaction.amount.toFloat()
                }
            }
            categoriesMap[category] = categoryTotalAmount/totalExpensesAmount.toFloat()
        }
        return categoriesMap
    }
    fun refresh() {}

}