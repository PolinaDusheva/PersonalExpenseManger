package com.example.personalexpensemanager.ui.dashboard

import androidx.compose.animation.core.Transition
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.ExpenseDataService
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dataService: ExpenseDataService
): ViewModel() {
    private val _uiState = MutableStateFlow<DashboardUIState>(DashboardUIState.Loading)
    val uiState : StateFlow<DashboardUIState> = _uiState

    init{load()}
    fun refresh() {
        load()
    }

    private fun load(){
        viewModelScope.launch {
            _uiState.value = DashboardUIState.Loading
            try {
                val transactions = dataService.getTransactions()
                val categories = dataService.getCategories()
                val categoriesMap = calculateCategoriesPercentage(categories,transactions)
                val totalAmount = calculateTotalAmount(transactions)
                val biggestExpense = calculateBiggestExpense(transactions)
                _uiState.value = DashboardUIState.Success(
                    transactions = transactions.reversed().take(5),
                    categoriesMap = categoriesMap,
                    totalAmount = totalAmount,
                    biggestExpense = biggestExpense
                )
            } catch (e: Exception) {
                _uiState.value = DashboardUIState.Error(e.message ?: "Error")
            }
        }
    }
    private fun calculateTotalAmount(transactions: List<Transaction>): Double {
        var amount: Double = 0.0
        for(currentTransaction in transactions) {
            var currAmount = currentTransaction.amount
            if (currentTransaction.sign == '-') {
                currAmount *= -1;
            }
            amount += currAmount
        }
        return amount
    }
    private fun calculateTotalExpenses(transactions: List<Transaction>): Double{
        var amount: Double = 0.0
        for(currentTransaction in transactions) {
            var currAmount = currentTransaction.amount
            if (currentTransaction.sign == '-') {
                amount += currAmount;
            }
        }
        return amount
    }
    private fun calculateBiggestExpense(transactions: List<Transaction>): Double {
        var maxExpense: Double = 0.0
        for(currentTransaction in transactions){
            var currAmount = currentTransaction.amount
            if(currentTransaction.sign == '-' && currAmount>maxExpense){
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
                if (category.id ==transaction.categoryId && transaction.sign == '-'){
                    categoryTotalAmount+=transaction.amount.toFloat()
                }
            }
            categoriesMap[category] = categoryTotalAmount/totalExpensesAmount.toFloat()
        }
        return categoriesMap
    }

}