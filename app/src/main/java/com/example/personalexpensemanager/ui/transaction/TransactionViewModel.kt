package com.example.personalexpensemanager.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class TransactionViewModel(
    private val dataService: IExpenseDataService
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _sortingType = MutableStateFlow(SortingType.NONE)

    val uiState: StateFlow<ITransactionUIState> = combine(
        dataService.transactions,
        dataService.categories,
        _selectedCategory,
        _sortingType
    ) { transactions, categories, selectedCategory, sortingType ->
        val reversedTransactions = transactions.reversed()
        val filtered = if (selectedCategory == null) {
            reversedTransactions
        } else {
            reversedTransactions.filter { it.categoryId == selectedCategory }
        }
        val sorted = applySorting(filtered, sortingType)
        ITransactionUIState.Success(
            transactions = reversedTransactions,
            filteredTransactions = sorted,
            categories = categories,
            selectedCategory = selectedCategory,
            sortingType = sortingType
        ) as ITransactionUIState
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ITransactionUIState.Loading
    )

    fun filterByCategory(categoryId: String?) {
        _selectedCategory.value = categoryId
    }

    fun sortBy(sortingType: SortingType) {
        _sortingType.value = if (_sortingType.value == sortingType) SortingType.NONE else sortingType
    }

    private fun applySorting(transactions: List<Transaction>, sortingType: SortingType): List<Transaction> {
        return when (sortingType) {
            SortingType.AMOUNT -> transactions.sortedByDescending { it.amount }
            SortingType.DATE -> transactions.sortedByDescending { it.date }
            SortingType.NONE -> transactions
        }
    }

    fun refresh() {}
}