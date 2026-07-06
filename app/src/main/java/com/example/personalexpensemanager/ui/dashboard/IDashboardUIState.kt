package com.example.personalexpensemanager.ui.dashboard

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import java.math.BigDecimal

sealed interface IDashboardUIState {
    data object Loading : IDashboardUIState
    data class Success(
        val transactions: List<Transaction>,
        val totalAmount: BigDecimal,
        val biggestExpense: BigDecimal,
        val categoriesMap: HashMap<Category, Float>
    ) : IDashboardUIState
    data class Error(val message: String) : IDashboardUIState
}
