package com.example.personalexpensemanager.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException

class GoalsViewModel(
    private val dataService: IExpenseDataService
) : ViewModel() {
    private val _snackbarEvent = MutableSharedFlow<Int>()
    val snackbarEvent: SharedFlow<Int> = _snackbarEvent.asSharedFlow()

    private val retrySignal = MutableStateFlow(0)

    val uiState: StateFlow<IGoalsUIState> = retrySignal.flatMapLatest {
        combine(
            dataService.goals,
            dataService.transactions
        ) { goals, transactions ->
            IGoalsUIState.Success(
                goals.map { goal ->
                    IGoalsUIState.GoalProgress(goal, currentAmountFor(goal, transactions))
                }
            ) as IGoalsUIState
        }.catch { e ->
            emit(IGoalsUIState.Error(R.string.error_load_goals))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IGoalsUIState.Loading
    )

    fun retry() { retrySignal.value++ }

    private fun currentAmountFor(goal: Goal, transactions: List<Transaction>): BigDecimal =
        transactions
            .filter { it.type == TransactionType.TRANSFER && it.goalId == goal.id }
            .sumOf { it.amount }

    fun addGoal(title: String, targetAmount: BigDecimal, deadline: LocalDate?) {
        viewModelScope.launch {
            try {
                dataService.addGoal(
                    Goal(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        targetAmount = targetAmount,
                        deadline = deadline
                    )
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _snackbarEvent.emit(R.string.error_add_goal)
            }
        }
    }

    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            try {
                dataService.deleteGoal(goalId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _snackbarEvent.emit(R.string.error_delete_goal)
            }
        }
    }


    fun addFunds(goalId: String, amount: BigDecimal) {
        viewModelScope.launch {
            try {
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
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _snackbarEvent.emit(R.string.error_add_funds)
            }
        }
    }
}