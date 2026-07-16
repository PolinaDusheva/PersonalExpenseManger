package com.example.personalexpensemanager.ui.transactionDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class TransactionDetailsViewModel(
    private val dataService: IExpenseDataService,
    private val transactionId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow<ITransactionDetailsUIState>(ITransactionDetailsUIState.Loading)
    val uiState: StateFlow<ITransactionDetailsUIState> = _uiState

    private val _snackbarEvent = MutableSharedFlow<Int>()
    val snackbarEvent: SharedFlow<Int> = _snackbarEvent.asSharedFlow()

    private val _deleted = MutableStateFlow(false)
    val deleted: StateFlow<Boolean> = _deleted

    init { load() }

    private fun load() {
        viewModelScope.launch {
            _uiState.value = ITransactionDetailsUIState.Loading
            try {
                val transaction = dataService.getTransaction(transactionId)
                if (transaction != null) {
                    val category = dataService.getCategories().find { it.id == transaction.categoryId }
                    val goal = dataService.goals.value.find { it.id == transaction.goalId }
                    _uiState.value = ITransactionDetailsUIState.Success(transaction, category, goal)
                } else {
                    _uiState.value = ITransactionDetailsUIState.Error(R.string.transaction_not_found)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = ITransactionDetailsUIState.Error(R.string.error_load_transaction_details)
            }
        }
    }

    fun retry() = load()

    fun deleteTransaction() {
        val currentState = _uiState.value
        viewModelScope.launch {
            try {
                dataService.deleteTransaction(transactionId)
                _deleted.value = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = currentState
                _snackbarEvent.emit(R.string.error_delete_transaction)
            }
        }
    }
}