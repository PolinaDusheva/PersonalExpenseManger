package com.example.personalexpensemanager.ui.transaction

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import java.time.LocalDate

import com.example.personalexpensemanager.ui.statistics.components.Period

sealed interface ITransactionUIState {
    data object Loading : ITransactionUIState
    data class Success(
        val transactions: List<Transaction>,
        val filteredTransactions: List<Transaction>,
        val categories: List<Category>,
        val categoriesById: Map<String, Category>,
        val selectedCategory: String?,
        val sortingType: SortingType,
        val selectedPeriod: Period? = null
    ) : ITransactionUIState {
        val groupedByDate: Map<LocalDate, List<Transaction>>?
            get() = if (sortingType == SortingType.DATE) {
                filteredTransactions.groupBy { it.date }
            } else null
    }
    data class Error(val messageResId: Int) : ITransactionUIState
}
