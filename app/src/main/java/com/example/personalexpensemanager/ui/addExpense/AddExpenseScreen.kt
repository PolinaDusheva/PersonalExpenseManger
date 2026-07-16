package com.example.personalexpensemanager.ui.addExpense

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.ui.components.AppSnackbarHost
import com.example.personalexpensemanager.ui.components.ErrorScreen
import com.example.personalexpensemanager.ui.components.appButtons.DialogConfirmButton
import com.example.personalexpensemanager.ui.components.appButtons.DialogDismissButton

@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel,
    onBack: () -> Unit,
    onNavigateToCategories: () -> Unit = {},
    onNavigateToGoals: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val formErrors by viewModel.formErrors.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val showLimitWarning by viewModel.showDailyLimitWarning.collectAsStateWithLifecycle()

    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var goals by remember { mutableStateOf<List<Goal>>(emptyList()) }
    val editingState = state as? IAddExpenseUIState.Editing
    if (editingState != null) {
        categories = editingState.categories
        goals = editingState.goals
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { resId ->
            snackbarHostState.showSnackbar(context.getString(resId))
        }
    }

    LaunchedEffect(state) {
        if (state is IAddExpenseUIState.Saved) {
            onBack()
        }
    }

    if (showLimitWarning) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissLimitWarning() },
            containerColor = Color.White,
            title = { Text(stringResource(R.string.daily_limit_exceeded_title)) },
            text = { Text(stringResource(R.string.daily_limit_exceeded_message)) },
            confirmButton = {
                DialogConfirmButton(
                    text = stringResource(R.string.daily_limit_exceeded_confirm),
                    onClick = { viewModel.confirmSaveOverLimit() }
                )
            },
            dismissButton = {
                DialogDismissButton(
                    text = stringResource(R.string.action_cancel),
                    onClick = { viewModel.dismissLimitWarning() }
                )
            }
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (state) {
                is IAddExpenseUIState.Error -> ErrorScreen(
                    messageResId = (state as IAddExpenseUIState.Error).messageResId,
                    onRetry = onBack
                )
                else -> AddExpenseForm(
                    state = formState,
                    formErrors = formErrors,
                    categories = categories,
                    goals = goals,
                    isSaving = state is IAddExpenseUIState.Saving,
                    isEdit = viewModel.isEditMode,
                    actions = viewModel,
                    onNavigateToCategories = onNavigateToCategories,
                    onNavigateToGoals = onNavigateToGoals
                )
            }
        }
    }
}