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
fun SetDailyLimitDialog(
    currentLimit: BigDecimal?,
    onConfirm: (BigDecimal) -> Unit,
    onDismiss: () -> Unit
) {
    var amountText by remember {
        mutableStateOf(currentLimit?.toPlainString() ?: "")
    }
    var touched by remember { mutableStateOf(false) }
    val isError = touched && (amountText.toBigDecimalOrNull() == null
            || amountText.toBigDecimalOrNull() == BigDecimal.ZERO)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text(stringResource(R.string.goals_daily_limit_title)) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it
                        touched = true
                    },
                    label = stringResource(R.string.goals_daily_limit_label),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                if (isError) {
                    Text(
                        text = stringResource(R.string.validation_daily_limit_required),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            DialogConfirmButton(
                text = stringResource(R.string.goal_save),
                onClick = {
                    val parsed = amountText.toBigDecimalOrNull()
                    if (parsed != null && parsed > BigDecimal.ZERO) {
                        onConfirm(parsed)
                    } else {
                        touched = true
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