package com.example.personalexpensemanager.ui.addExpense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state is AddExpenseUIState.Saved) {
        onBack()
    }

    AddExpenseForm(
        categories = (state as? AddExpenseUIState.Editing)?.categories ?: emptyList(),
        isSaving = state is AddExpenseUIState.Loading,
        onSave = { title, amount, categoryId, date, description, paymentMethod ->
            viewModel.addExpense(title, amount, categoryId, date, description, paymentMethod)
        },
        onBack = onBack
    )
}