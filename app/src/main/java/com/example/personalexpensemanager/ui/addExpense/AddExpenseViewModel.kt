package com.example.personalexpensemanager.ui.addExpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

class AddExpenseViewModel(
    private val dataService: IExpenseDataService
) : ViewModel() {

    private val _uiState = MutableStateFlow<IAddExpenseUIState>(IAddExpenseUIState.Loading)
    val uiState: StateFlow<IAddExpenseUIState> = _uiState

    init {
        viewModelScope.launch {
            combine(dataService.categories, dataService.goals) { categories, goals -> categories to goals }
                .collect { (categories, goals) ->
                    val current = _uiState.value
                    if (current !is IAddExpenseUIState.Saving && current !is IAddExpenseUIState.Saved) {
                        _uiState.value = IAddExpenseUIState.Editing(categories, goals)
                    }
                }
        }
    }

    fun addExpense(
        title: String,
        amount: BigDecimal,
        categoryId: String?,
        date: LocalDate,
        description: String,
        paymentMethod: PaymentMethod,
        transactionType: TransactionType,
        goalId: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = IAddExpenseUIState.Saving
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
                    paymentMethod = paymentMethod,
                    goalId = goalId
                )
                dataService.addTransaction(transaction)
                _uiState.value = IAddExpenseUIState.Saved
            } catch (e: Exception) {
                _uiState.value = IAddExpenseUIState.Error(e.message ?: "Грешка")
            }
        }
    }
}