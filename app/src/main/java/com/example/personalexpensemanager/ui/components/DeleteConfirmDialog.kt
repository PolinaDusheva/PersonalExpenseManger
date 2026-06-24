package com.example.personalexpensemanager.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.personalexpensemanager.ui.components.appButtons.AppTextButton

@Composable
fun DeleteConfirmDialog(
    categoryName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Изтриване") },
        text = { Text("Сигурни ли сте, че искате да изтриете „$categoryName\"?") },
            confirmButton = {
                AppTextButton(text = "Изтрий", onClick = onConfirm)
            },
            dismissButton = {
                AppTextButton(text = "Отказ", onClick = onDismiss)
            }
        )
}

