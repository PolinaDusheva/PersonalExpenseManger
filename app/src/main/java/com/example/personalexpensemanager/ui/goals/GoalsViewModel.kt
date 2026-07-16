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
import com.example.personalexpensemanager.domain.validation.notEmpty
import com.example.personalexpensemanager.domain.validation.positiveAmount
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
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
            dataService.transactions,
            dataService.monthlyBudget,
            dataService.dailyLimit
        ) { goals, transactions, budget, dailyLimit ->
            IGoalsUIState.Success(
                goals = buildGoalProgressList(goals, transactions),
                monthlyBudget = budget,
                totalSpentThisMonth = calculateCurrentMonthExpenses(transactions),
                dailyLimit = dailyLimit,
                totalSpentToday = calculateTodayExpenses(transactions)
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

    private fun buildGoalProgressList(
        goals: List<Goal>,
        transactions: List<Transaction>
    ): List<IGoalsUIState.GoalProgress> {
        val result = mutableListOf<IGoalsUIState.GoalProgress>()
        for (goal in goals) {
            result.add(IGoalsUIState.GoalProgress(goal, currentAmountFor(goal, transactions)))
        }
        return result
    }

    private fun currentAmountFor(goal: Goal, transactions: List<Transaction>): BigDecimal {
        var total = BigDecimal.ZERO
        for (transaction in transactions) {
            if (transaction.type == TransactionType.TRANSFER && transaction.goalId == goal.id) {
                total += transaction.amount
            }
        }
        return total
    }

    private fun calculateCurrentMonthExpenses(transactions: List<Transaction>): BigDecimal {
        val currentMonth = YearMonth.now()
        var total = BigDecimal.ZERO
        for (transaction in transactions) {
            if (transaction.type == TransactionType.EXPENSE &&
                YearMonth.from(transaction.date) == currentMonth) {
                total += transaction.amount
            }
        }
        return total
    }
    private fun calculateTodayExpenses(transactions: List<Transaction>): BigDecimal {
        val today = LocalDate.now()
        var total = BigDecimal.ZERO
        for (transaction in transactions) {
            if (transaction.type == TransactionType.EXPENSE && transaction.date == today) {
                total += transaction.amount
            }
        }
        return total
    }

    fun setDailyLimit(amount: BigDecimal) {
        viewModelScope.launch {
            try {
                dataService.setDailyLimit(amount)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _snackbarEvent.emit(R.string.error_generic)
            }
        }
    }

    fun setMonthlyBudget(amount: BigDecimal) {
        viewModelScope.launch {
            try {
                dataService.setMonthlyBudget(amount)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _snackbarEvent.emit(R.string.error_generic)
            }
        }
    }

    private val _formErrors = MutableStateFlow(GoalFormErrors())
    val formErrors: StateFlow<GoalFormErrors> = _formErrors.asStateFlow()

    private val _titleInput = MutableStateFlow("")
    private val _amountInput = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _titleInput.debounce(300).collect { value ->
                val error = notEmpty(value, R.string.validation_goal_title_empty)
                _formErrors.update { it.copy(titleErrorResId = error) }
            }
        }
        viewModelScope.launch {
            _amountInput.debounce(300).collect { value ->
                val error = positiveAmount(value.toBigDecimalOrNull(), R.string.validation_goal_amount_required)
                _formErrors.update { it.copy(amountErrorResId = error) }
            }
        }
    }

    fun resetForm() {
        _formErrors.value = GoalFormErrors()
        _titleInput.value = ""
        _amountInput.value = ""
    }

    fun onGoalTitleChanged(value: String) { _titleInput.value = value }
    fun onGoalTitleTouched() { _formErrors.update { it.copy(titleTouched = true) } }
    fun onGoalAmountChanged(value: String) { _amountInput.value = value }
    fun onGoalAmountTouched() { _formErrors.update { it.copy(amountTouched = true) } }

    fun addGoal(
        title: String,
        targetAmount: BigDecimal,
        deadline: LocalDate?
    ): Boolean {
        val titleError = notEmpty(title, R.string.validation_goal_title_empty)
        val amountError = positiveAmount(targetAmount, R.string.validation_goal_amount_required)

        _formErrors.value = GoalFormErrors(
            titleErrorResId = titleError,
            amountErrorResId = amountError,
            titleTouched = true,
            amountTouched = true
        )

        if (titleError != null || amountError != null) return false

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
        return true
    }

    fun prepareEditForm(goal: Goal) {
        _titleInput.value = goal.title
        _amountInput.value = goal.targetAmount.toPlainString()
        _formErrors.value = GoalFormErrors()
    }

    fun updateGoal(goalId: String, title: String, targetAmount: BigDecimal, deadline: LocalDate?): Boolean {
        val titleError = notEmpty(title, R.string.validation_goal_title_empty)
        val amountError = positiveAmount(targetAmount, R.string.validation_goal_amount_required)

        _formErrors.value = GoalFormErrors(
            titleErrorResId = titleError,
            amountErrorResId = amountError,
            titleTouched = true,
            amountTouched = true
        )

        if (titleError != null || amountError != null) return false

        viewModelScope.launch {
            try {
                dataService.updateGoal(
                    Goal(
                        id = goalId,
                        title = title,
                        targetAmount = targetAmount,
                        deadline = deadline
                    )
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _snackbarEvent.emit(R.string.error_update_goal)
            }
        }
        return true
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
}