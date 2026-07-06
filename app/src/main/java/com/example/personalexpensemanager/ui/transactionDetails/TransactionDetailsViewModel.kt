package com.example.personalexpensemanager.ui.transactionDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionDetailsViewModel(
    private val dataService: IExpenseDataService,
    private val transactionId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow<ITransactionDetailsUIState>(ITransactionDetailsUIState.Loading)
    val uiState: StateFlow<ITransactionDetailsUIState> = _uiState

    init { load() }

    private fun load() {
        viewModelScope.launch {
            try {
                val transaction = dataService.getTransaction(transactionId)
                if (transaction != null) {
                    val category = dataService.getCategories().find { it.id == transaction.categoryId }
                    _uiState.value = ITransactionDetailsUIState.Success(transaction, category)
                } else {
                    _uiState.value = ITransactionDetailsUIState.Error(R.string.transaction_not_found)
                }
            } catch (e: Exception) {
                _uiState.value = ITransactionDetailsUIState.Error(R.string.generic_error)
            }
        }
    }
}