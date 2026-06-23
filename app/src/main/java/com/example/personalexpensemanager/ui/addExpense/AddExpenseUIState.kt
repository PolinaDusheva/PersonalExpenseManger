package com.example.personalexpensemanager.ui.addExpense

import com.example.personalexpensemanager.domain.Category

sealed interface AddExpenseUIState {
    data object Loading : AddExpenseUIState
    data class Editing(val categories: List<Category>) : AddExpenseUIState
    data object Saving : AddExpenseUIState
    data object Saved : AddExpenseUIState
    data class Error(val message: String) : AddExpenseUIState
}