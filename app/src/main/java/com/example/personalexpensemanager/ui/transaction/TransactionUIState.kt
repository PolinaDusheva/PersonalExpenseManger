package com.example.personalexpensemanager.ui.transaction

import com.example.personalexpensemanager.domain.Transaction

sealed interface TransactionUIState {
    data object Loading : TransactionUIState
    data class Success(
        val transactions: List<Transaction>,
        val filteredTransactions: List<Transaction>,
        val selectedCategory: String?
    ) : TransactionUIState
    data class Error(val message: String) : TransactionUIState
}