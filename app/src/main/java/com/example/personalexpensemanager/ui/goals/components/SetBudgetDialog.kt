package com.example.personalexpensemanager.ui.goals.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.addExpense.components.TextField
import com.example.personalexpensemanager.ui.components.appButtons.DialogConfirmButton
import com.example.personalexpensemanager.ui.components.appButtons.DialogDismissButton
import java.math.BigDecimal

@Composable
fun SetBudgetDialog(
    currentBudget: BigDecimal?,
    onConfirm: (BigDecimal) -> Unit,
    onDismiss: () -> Unit
) {
    var amountText by remember {
        mutableStateOf(currentBudget?.toPlainString() ?: "")
    }
    var touched by remember { mutableStateOf(false) }
    val parsed = amountText.toBigDecimalOrNull()
    val error = if (touched && (parsed == null || parsed <= BigDecimal.ZERO))
        R.string.validation_budget_required else null

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text(stringResource(R.string.goals_budget_title)) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it
                        touched = true
                    },
                    label = stringResource(R.string.goals_budget_label),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                if (error != null) {
                    Text(
                        text = stringResource(error),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            DialogConfirmButton(
                text = stringResource(R.string.action_ok),
                onClick = {
                    touched = true
                    val value = amountText.toBigDecimalOrNull()
                    if (value != null && value > BigDecimal.ZERO) {
                        onConfirm(value)
                    }
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