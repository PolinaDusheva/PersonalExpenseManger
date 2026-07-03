package com.example.personalexpensemanager.domain.enums

enum class TransactionType {
    EXPENSE, INCOME
}
val TransactionType.signSymbol: String
    get() = when (this) {
        TransactionType.EXPENSE -> "-"
        TransactionType.INCOME -> "+"
    }