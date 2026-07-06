package com.example.personalexpensemanager.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

class GoalsViewModel(
    private val dataService: IExpenseDataService
) : ViewModel() {

    val uiState: StateFlow<IGoalsUIState> = combine(
        dataService.goals,
        dataService.transactions
    ) { goals, transactions ->
        IGoalsUIState.Success(
            goals.map { goal ->
                GoalProgress(
                    goal,
                    currentAmountFor(goal, transactions)
                )
            }
        ) as IGoalsUIState
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IGoalsUIState.Loading
    )

    private fun currentAmountFor(goal: Goal, transactions: List<Transaction>): BigDecimal =
        transactions
            .filter { it.type == TransactionType.TRANSFER && it.goalId == goal.id }
            .sumOf { it.amount }

    fun addGoal(title: String, targetAmount: BigDecimal, deadline: LocalDate?) {
        viewModelScope.launch {
            dataService.addGoal(
                Goal(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    targetAmount = targetAmount,
                    deadline = deadline
                )
            )
        }
    }

    fun deleteGoal(goalId: String) {
        viewModelScope.launch { dataService.deleteGoal(goalId) }
    }

    fun addFunds(goalId: String, amount: BigDecimal) {
        viewModelScope.launch {
            dataService.addTransaction(
                Transaction(
                    id = UUID.randomUUID().toString(),
                    title = "Goal contribution",
                    amount = amount,
                    date = LocalDate.now(),
                    currency = Currency.EUR,
                    type = TransactionType.TRANSFER,
                    categoryId = null,
                    description = "Прехвърляне към цел",
                    paymentMethod = PaymentMethod.CARD,
                    goalId = goalId
                )
            )
        }
    }
}