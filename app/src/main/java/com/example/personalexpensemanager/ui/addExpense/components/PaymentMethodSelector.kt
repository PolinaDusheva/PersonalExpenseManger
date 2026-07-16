package com.example.personalexpensemanager.ui.addExpense.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.enums.PaymentMethod

@Composable
fun PaymentMethodSelector(
    selected: PaymentMethod,
    onSelect: (PaymentMethod) -> Unit
) {
    Column {
        Text(stringResource(R.string.payment_method))
        PaymentMethod.entries.forEach { method ->
            RadioButton(
                option = method,
                selected = method == selected,
                label = if (method == PaymentMethod.CARD)
                    stringResource(R.string.payment_method_card)
                else
                    stringResource(R.string.payment_method_cash),
                onSelect = onSelect
            )
        }
    }
}