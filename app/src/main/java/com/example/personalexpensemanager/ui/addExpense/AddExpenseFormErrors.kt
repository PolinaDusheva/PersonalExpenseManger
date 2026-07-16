package com.example.personalexpensemanager.ui.addExpense

data class AddExpenseFormErrors(
    val titleErrorResId: Int? = null,
    val amountErrorResId: Int? = null,
    val descriptionErrorResId: Int? = null,
    val dateErrorResId: Int? = null,
    val categoryErrorResId: Int? = null,
    val goalErrorResId: Int? = null,
    val submitted: Boolean = false
)