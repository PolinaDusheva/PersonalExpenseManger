package com.example.personalexpensemanager.ui.dashboard

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction

sealed interface DashboardUIState {
    data object Loading : DashboardUIState
    data class Success(
        val transactions: List<Transaction>,
        val totalAmount: Double,
        val biggestExpense: Double,
        val categoriesMap: HashMap<Category, Float>
    ) : DashboardUIState
    data class Error(val message: String) : DashboardUIState
}
