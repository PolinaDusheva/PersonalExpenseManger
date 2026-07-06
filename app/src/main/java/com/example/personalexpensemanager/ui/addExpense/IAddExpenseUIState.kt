package com.example.personalexpensemanager.ui.addExpense

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Goal

sealed interface IAddExpenseUIState {
    data object Loading : IAddExpenseUIState
    data class Editing(val categories: List<Category>, val goals: List<Goal> = emptyList()) : IAddExpenseUIState
    data object Saving : IAddExpenseUIState
    data object Saved : IAddExpenseUIState
    data class Error(val message: String) : IAddExpenseUIState
}