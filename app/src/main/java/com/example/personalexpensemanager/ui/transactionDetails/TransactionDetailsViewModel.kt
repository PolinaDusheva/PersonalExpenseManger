package com.example.personalexpensemanager.ui.transactionDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.ExpenseDataService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TransactionDetailsViewModel(
    private val dataService : ExpenseDataService,
    private val transactionId : String
): ViewModel() {
    private val _uiState = MutableStateFlow<TransactionDetailsUIState>(TransactionDetailsUIState.Loading)
    val uiState: StateFlow<TransactionDetailsUIState> = _uiState

    init { load() }

    private fun load() {
        viewModelScope.launch {
            try {
                val transaction = dataService.getTransaction(transactionId)
                if (transaction != null) {
                    _uiState.value = TransactionDetailsUIState.Success(transaction)
                } else {
                    _uiState.value = TransactionDetailsUIState.Error("Транзакцията не е намерена")
                }
            } catch (e: Exception) {
                _uiState.value = TransactionDetailsUIState.Error(e.message ?: "Грешка")
            }
        }
    }


}