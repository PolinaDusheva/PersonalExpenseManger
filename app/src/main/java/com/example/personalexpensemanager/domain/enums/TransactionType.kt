package com.example.personalexpensemanager.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Savings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.personalexpensemanager.R

enum class TransactionType {
    EXPENSE, INCOME, TRANSFER
}

val TransactionType.signSymbol: String
    get() = when (this) {
        TransactionType.EXPENSE -> "-"
        TransactionType.INCOME -> "+"
        TransactionType.TRANSFER -> "-"
    }

val TransactionType.amountColorRes: Int
    get() = when (this) {
        TransactionType.INCOME -> R.color.transaction_income
        TransactionType.EXPENSE -> R.color.transaction_expense
        TransactionType.TRANSFER -> R.color.transaction_transfer
    }

val TransactionType.circleColorRes: Int
    get() = when (this) {
        TransactionType.INCOME -> R.color.transaction_income_circle
        TransactionType.EXPENSE -> R.color.transaction_expense_circle
        TransactionType.TRANSFER -> R.color.transaction_transfer_circle
    }

val TransactionType.arrowColorRes: Int
    get() = when (this) {
        TransactionType.INCOME -> R.color.transaction_income_arrow
        TransactionType.EXPENSE -> R.color.transaction_expense
        TransactionType.TRANSFER -> R.color.transaction_transfer
    }

val TransactionType.icon: ImageVector
    get() = when (this) {
        TransactionType.INCOME -> Icons.Filled.KeyboardArrowDown
        TransactionType.EXPENSE -> Icons.Filled.KeyboardArrowUp
        TransactionType.TRANSFER -> Icons.Filled.Savings
    }