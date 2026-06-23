package com.example.personalexpensemanager.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.ExpenseDataService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val dataService: ExpenseDataService
) : ViewModel() {

    private val _uiState = MutableStateFlow<TransactionUIState>(TransactionUIState.Loading)
    val uiState: StateFlow<TransactionUIState> = _uiState

    init { load() }

    fun refresh() {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = TransactionUIState.Loading
            try {
                val transactions = dataService.getTransactions()
                val categories = dataService.getCategories()
                _uiState.value = TransactionUIState.Success(
                    transactions = transactions,
                    filteredTransactions = transactions,
                    categories = categories,
                    selectedCategory = null,
                    sortingType = SortingType.NONE
                )
            } catch (e: Exception) {
                _uiState.value = TransactionUIState.Error(e.message ?: "Грешка")
            }
        }
    }

    fun filterByCategory(categoryId: String?) {
        val current = _uiState.value as? TransactionUIState.Success ?: return
        val filtered = if (categoryId == null) {
            current.transactions
        } else {
            current.transactions.filter { it.categoryId == categoryId }
        }
        _uiState.value = current.copy(
            filteredTransactions = filtered,
            selectedCategory = categoryId
        )
    }

    fun sortBy(sortingType: SortingType) {
        val current = _uiState.value as? TransactionUIState.Success ?: return
        val sorted = when (sortingType) {
            SortingType.AMOUNT -> current.filteredTransactions.sortedByDescending { it.amount }
            SortingType.DATE -> current.filteredTransactions.sortedByDescending { it.date }
            SortingType.NONE -> current.filteredTransactions
        }
        _uiState.value = current.copy(
            filteredTransactions = sorted,
            sortingType = sortingType
        )
    }


    }