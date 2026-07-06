package com.example.personalexpensemanager.ui.goals

import com.example.personalexpensemanager.domain.Goal
import java.math.BigDecimal

data class GoalProgress(
    val goal: Goal,
    val currentAmount: BigDecimal
)

sealed interface IGoalsUIState {
    data object Loading : IGoalsUIState
    data class Success(val goals: List<GoalProgress>) : IGoalsUIState
    data class Error(val message: String) : IGoalsUIState
}