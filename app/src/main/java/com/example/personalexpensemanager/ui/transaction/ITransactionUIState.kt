package com.example.personalexpensemanager.ui.transaction

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import java.time.LocalDate

sealed interface ITransactionUIState {
    data object Loading : ITransactionUIState
    data class Success(
        val transactions: List<Transaction>,
        val filteredTransactions: List<Transaction>,
        val categories: List<Category>,
        val selectedCategory: String?,
        val sortingType: SortingType
    ) : ITransactionUIState{
        val groupedByDate: Map<LocalDate, List<Transaction>>?
            get() = if (sortingType == SortingType.DATE) {
                filteredTransactions.groupBy { it.date }
            } else null
    }
    data class Error(val message: String) : ITransactionUIState
}
