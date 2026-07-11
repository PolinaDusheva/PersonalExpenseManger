package com.example.personalexpensemanager.ui.transactionDetails

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.domain.Transaction

sealed interface ITransactionDetailsUIState {
    data object Loading: ITransactionDetailsUIState
    data class Success(
        val transaction: Transaction,
        val category: Category?,
        val goal: Goal?
    ): ITransactionDetailsUIState
    data class Error(val messageResId: Int) : ITransactionDetailsUIState
}