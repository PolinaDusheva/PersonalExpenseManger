package com.example.personalexpensemanager.ui.addExpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.ExpenseDataService
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class AddExpenseViewModel(
    private val dataService: ExpenseDataService
): ViewModel() {

    private val _uiState = MutableStateFlow<AddExpenseUIState>(
        AddExpenseUIState.Loading)
    val uiState: StateFlow<AddExpenseUIState> = _uiState
    init { loadCategories() }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = dataService.getCategories()
                _uiState.value = AddExpenseUIState.Editing(categories)
            } catch (e: Exception) {
                _uiState.value = AddExpenseUIState.Error(e.message ?: "Грешка")
            }
        }
    }

    fun addExpense(
        title: String,
        amount: Double,
        categoryId: String,
        date: LocalDate,
        description: String,
        paymentMethod: PaymentMethod
    ) {
        viewModelScope.launch {
            _uiState.value = AddExpenseUIState.Saving
            try {
                val transaction = Transaction(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    amount = amount,
                    date = date,
                    currency = '€',
                    sign = '-',
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