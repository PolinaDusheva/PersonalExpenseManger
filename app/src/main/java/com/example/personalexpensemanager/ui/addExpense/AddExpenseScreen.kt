package com.example.personalexpensemanager.ui.addExpense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        if (state is IAddExpenseUIState.Saved) {
            onBack()
        }
    }

    AddExpenseForm(
        categories = (state as? IAddExpenseUIState.Editing)?.categories ?: emptyList(),
        goals = (state as? IAddExpenseUIState.Editing)?.goals ?: emptyList(),
        isSaving = state is IAddExpenseUIState.Saving,
        onSave = {
            title,
            amount,
            categoryId,
            date,
            description,
            paymentMethod,
            transactionType,
            goalId ->
            viewModel.addExpense(
                title,
                amount,
                categoryId,
                date,
                description,
                paymentMethod,
                transactionType,
                goalId)
        }
    )
}