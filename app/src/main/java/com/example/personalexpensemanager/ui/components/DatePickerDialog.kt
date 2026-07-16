package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.components.appButtons.DialogConfirmButton
import com.example.personalexpensemanager.ui.components.appButtons.DialogDismissButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    confirmText: String = stringResource(R.string.action_ok),
    dismissText: String = stringResource(R.string.action_cancel),
    content: @Composable ColumnScope.() -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        colors = DatePickerDefaults.colors(containerColor = Color.White),
        confirmButton = {
            DialogConfirmButton(text = confirmText, onClick = onConfirm)
        },
        dismissButton = {
            DialogDismissButton(text = dismissText, onClick = onDismissRequest)
        }
    ) {
        content()
    }
}

@Composable
fun appDatePickerColors() = DatePickerDefaults.colors(containerColor = Color.White)