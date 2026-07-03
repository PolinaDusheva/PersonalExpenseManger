package com.example.personalexpensemanager.ui.addExpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.ExpenseDataService
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class AddExpenseViewModel(
    private val dataService: ExpenseDataService
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddExpenseUIState>(AddExpenseUIState.Loading)
    val uiState: StateFlow<AddExpenseUIState> = _uiState

    init {
        viewModelScope.launch {
            dataService.categories.collect { categories ->
                val current = _uiState.value
                if (current !is AddExpenseUIState.Saving && current !is AddExpenseUIState.Saved) {
                    _uiState.value = AddExpenseUIState.Editing(categories)
                }
            }
        }
    }

    fun addExpense(
        title: String,
        amount: Double,
        categoryId: String,
        date: LocalDate,
        description: String,
        paymentMethod: PaymentMethod,
        transactionType: TransactionType
    ) {
        viewModelScope.launch {
            _uiState.value = AddExpenseUIState.Saving
            try {
                val transaction = Transaction(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    amount = amount,
                    date = date,
                    currency = Currency.EUR,
                    type = transactionType,
                    categoryId = categoryId,
                    description = description,
                    paymentMethod = paymentMethod
                )
                dataService.addTransaction(transaction)
                _uiState.value = AddExpenseUIState.Saved
            } catch (e: Exception) {
                _uiState.value = AddExpenseUIState.Error(e.message ?: "Грешка")
            }
        }
    }
}