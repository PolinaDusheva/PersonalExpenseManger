package com.example.personalexpensemanager.ui.transactionDetails

import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.ui.transaction.TransactionUIState

sealed interface TransactionDetailsUIState {
    data object Loading: TransactionDetailsUIState
    data class Success(
        val singleTransaction : Transaction
    ): TransactionDetailsUIState
    data class Error(val message: String) : TransactionDetailsUIState
}