package com.example.personalexpensemanager.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.components.appButtons.AppTextButton

@Composable
fun DeleteConfirmDialog(
    categoryName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_confirm_title)) },
        text = { Text(stringResource(R.string.delete_confirm_message, categoryName)) },
            confirmButton = {
                AppTextButton(text = stringResource(R.string.category_delete), onClick = onConfirm)
            },
            dismissButton = {
                AppTextButton(text = stringResource(R.string.category_cancel), onClick = onDismiss)
            }
        )
}

