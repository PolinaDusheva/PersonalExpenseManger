package com.example.personalexpensemanager.ui.transactionDetails

import androidx.lifecycle.ViewModel
import com.example.personalexpensemanager.data.ExpenseDataService
import com.example.personalexpensemanager.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class TransactionDetailsViewModel(
    private val dataService : ExpenseDataService,
    private val transactionId : String
): ViewModel() {
    private val _uiState = MutableStateFlow<TransactionDetailsUIState>(TransactionDetailsUIState.Loading)
    val uiState: StateFlow<TransactionDetailsUIState> = _uiState

    init { load() }

    fun load(){

    }


}