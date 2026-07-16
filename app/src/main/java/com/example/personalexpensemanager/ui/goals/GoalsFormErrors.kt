package com.example.personalexpensemanager.ui.goals

data class GoalFormErrors(
    val titleErrorResId: Int? = null,
    val amountErrorResId: Int? = null,
    val titleTouched: Boolean = false,
    val amountTouched: Boolean = false
)
