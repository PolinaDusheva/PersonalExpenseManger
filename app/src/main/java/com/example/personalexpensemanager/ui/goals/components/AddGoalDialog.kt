package com.example.personalexpensemanager.ui.goals.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.ui.addExpense.components.TextField
import com.example.personalexpensemanager.ui.components.DateField
import com.example.personalexpensemanager.ui.components.appButtons.DialogConfirmButton
import com.example.personalexpensemanager.ui.components.appButtons.DialogDismissButton
import com.example.personalexpensemanager.ui.goals.GoalFormErrors
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun AddGoalDialog(
    formErrors: GoalFormErrors,
    onTitleChanged: (String) -> Unit,
    onTitleTouched: () -> Unit,
    onAmountChanged: (String) -> Unit,
    onAmountTouched: () -> Unit,
    onConfirm: (title: String, targetAmount: BigDecimal, deadline: LocalDate?) -> Unit,
    onDismiss: () -> Unit,
    editingGoal: Goal? = null,
    onDelete: ((String) -> Unit)? = null
) {
    val isEdit = editingGoal != null
    var title by remember { mutableStateOf(editingGoal?.title ?: "") }
    var amountText by remember { mutableStateOf(editingGoal?.targetAmount?.toPlainString() ?: "") }
    var deadline by remember { mutableStateOf(editingGoal?.deadline) }
    var titleWasFocused by remember { mutableStateOf(false) }
    var amountWasFocused by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            var showDeleteConfirm by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isEdit) stringResource(R.string.goal_edit_title)
                    else stringResource(R.string.goal_add_title)
                )
                if (isEdit && onDelete != null) {
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            if (showDeleteConfirm) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirm = false },
                    containerColor = Color.White,
                    title = { Text(stringResource(R.string.delete_confirm_title)) },
                    text = { Text(stringResource(R.string.delete_confirm_message, editingGoal!!.title)) },
                    confirmButton = {
                        DialogConfirmButton(
                            text = stringResource(R.string.delete_confirm_title),
                            onClick = {
                                onDelete?.invoke(editingGoal!!.id)
                                showDeleteConfirm = false
                                onDismiss()
                            }
                        )
                    },
                    dismissButton = {
                        DialogDismissButton(
                            text = stringResource(R.string.action_cancel),
                            onClick = { showDeleteConfirm = false }
                        )
                    }
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Column {
                    TextField(
                        value = title,
                        onValueChange = {
                            title = it
                            onTitleChanged(it)
                        },
                        label = stringResource(R.string.goal_name_label),
                        modifier = Modifier.onFocusChanged { focusState ->
                            if (titleWasFocused && !focusState.isFocused) onTitleTouched()
                            titleWasFocused = focusState.isFocused
                        }
                    )
                    if (formErrors.titleTouched && formErrors.titleErrorResId != null) {
                        Text(
                            text = stringResource(formErrors.titleErrorResId),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))

                Column {
                    TextField(
                        value = amountText,
                        onValueChange = {
                            amountText = it
                            onAmountChanged(it)
                        },
                        label = stringResource(R.string.goal_amount_label),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.onFocusChanged { focusState ->
                            if (amountWasFocused && !focusState.isFocused) onAmountTouched()
                            amountWasFocused = focusState.isFocused
                        }
                    )
                    if (formErrors.amountTouched && formErrors.amountErrorResId != null) {
                        Text(
                            text = stringResource(formErrors.amountErrorResId),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))

                DateField(
                    date = deadline,
                    onDateSelected = { deadline = it }
                )
            }
        },
        confirmButton = {
            DialogConfirmButton(
                text = stringResource(R.string.goal_save),
                onClick = {
                    onConfirm(title, amountText.toBigDecimalOrNull() ?: BigDecimal.ZERO, deadline)
                }
            )
        },
        dismissButton = {
            DialogDismissButton(
                text = stringResource(R.string.action_cancel),
                onClick = onDismiss
            )
        }
    )
}