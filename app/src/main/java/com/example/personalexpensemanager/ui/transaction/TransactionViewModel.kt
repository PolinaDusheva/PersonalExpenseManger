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

    fun load() {
        viewModelScope.launch {
            _uiState.value = TransactionUIState.Loading
            try {
                val transactions = dataService.getTransactions()
                _uiState.value = TransactionUIState.Success(
                    transactions = transactions,
                    filteredTransactions = transactions,
                    selectedCategory = null
                )
            } catch (e: Exception) {
                _uiState.value = TransactionUIState.Error(e.message ?: "Грешка")
            }
        }
    }
    fun filterByCategory(categoryId: String?) {
        val current = _uiState.value as? TransactionUIState.Success ?: return
        _uiState.value = current.copy(
            filteredTransactions = if (categoryId == null) current.transactions
            else current.transactions.filter { it.categoryId == categoryId },
            selectedCategory = categoryId
        )
    }
}