package com.example.personalexpensemanager.ui.goals

data class GoalFormErrors(
    val titleErrorResId: Int? = null,
    val amountErrorResId: Int? = null,
    val submitted: Boolean = false
)
