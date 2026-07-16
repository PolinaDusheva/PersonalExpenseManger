package com.example.personalexpensemanager.ui.addExpense.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.enums.TransactionType

@Composable
fun TransactionTypeSelector(
    selected: TransactionType,
    onSelect: (TransactionType) -> Unit
) {
    Column {
        Text(stringResource(R.string.transaction_type))
        TransactionType.entries.forEach { type ->
            RadioButton(
                option = type,
                selected = type == selected,
                label = when (type) {
                    TransactionType.EXPENSE -> stringResource(R.string.transaction_type_expense)
                    TransactionType.INCOME -> stringResource(R.string.transaction_type_income)
                    TransactionType.TRANSFER -> stringResource(R.string.transaction_type_transfer)
                },
                onSelect = onSelect
            )
        }
    }
}