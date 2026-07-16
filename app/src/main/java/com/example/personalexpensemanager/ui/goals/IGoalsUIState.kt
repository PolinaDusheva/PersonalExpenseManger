package com.example.personalexpensemanager.ui.goals

import com.example.personalexpensemanager.domain.Goal
import java.math.BigDecimal

sealed interface IGoalsUIState {
    data object Loading : IGoalsUIState
    data class Success(
        val goals: List<GoalProgress>,
        val monthlyBudget: BigDecimal?,
        val totalSpentThisMonth: BigDecimal,
        val dailyLimit: BigDecimal?,
        val totalSpentToday: BigDecimal
    ) : IGoalsUIState
    data class Error(val messageResId: Int) : IGoalsUIState

    data class GoalProgress(
        val goal: Goal,
        val currentAmount: BigDecimal
    )
}