package com.example.personalexpensemanager.ui.addExpense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.ui.components.ErrorScreen

@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val formErrors by viewModel.formErrors.collectAsStateWithLifecycle()
    val existingTransaction by viewModel.existingTransaction.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        if (state is IAddExpenseUIState.Saved) {
            onBack()
        }
    }

    when (state) {
        is IAddExpenseUIState.Error -> ErrorScreen(
            messageResId = (state as IAddExpenseUIState.Error).messageResId,
            onRetry = onBack
        )
        else -> AddExpenseForm(
            categories = (state as? IAddExpenseUIState.Editing)?.categories ?: emptyList(),
            goals = (state as? IAddExpenseUIState.Editing)?.goals ?: emptyList(),
            isSaving = state is IAddExpenseUIState.Saving,
            formErrors = formErrors,
            existingTransaction = existingTransaction,
            onAmountChanged = viewModel::onAmountChanged,
            onAmountTouched = viewModel::onAmountTouched,
            onTitleChanged = viewModel::onTitleChanged,
            onTitleTouched = viewModel::onTitleTouched,
            onDescriptionChanged = viewModel::onDescriptionChanged,
            onDescriptionTouched = viewModel::onDescriptionTouched,
            onDateSelected = viewModel::onDateSelected,
            onCategorySelected = viewModel::onCategorySelected,
            onSave = viewModel::addExpense
        )
    }
}