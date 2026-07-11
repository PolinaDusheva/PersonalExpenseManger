package com.example.personalexpensemanager.ui.addExpense

data class AddExpenseFormErrors(
    val titleErrorResId: Int? = null,
    val amountErrorResId: Int? = null,
    val descriptionErrorResId: Int? = null,
    val dateErrorResId: Int? = null,
    val categoryErrorResId: Int? = null,
    val titleTouched: Boolean = false,
    val amountTouched: Boolean = false,
    val descriptionTouched: Boolean = false,
    val dateTouched: Boolean = false,
    val categoryTouched: Boolean = false
)