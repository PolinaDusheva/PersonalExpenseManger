package com.example.personalexpensemanager.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.domain.Transaction
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

    private val _formErrors = MutableStateFlow(GoalFormErrors())
    val formErrors: StateFlow<GoalFormErrors> = _formErrors.asStateFlow()

    private val _titleInput = MutableStateFlow("")
    private val _amountInput = MutableStateFlow("")

    val uiState: StateFlow<IGoalsUIState> = retrySignal.flatMapLatest {
        combine(
            dataService.goals,
            dataService.transactions,
            dataService.monthlyBudget,
            dataService.dailyLimit
        ) { goals, transactions, budget, dailyLimit ->
            val goalProgressList = buildGoalProgressList(goals, transactions)
            IGoalsUIState.Success(
                goals = goalProgressList,
                monthlyBudget = budget,
                totalSpentThisMonth = calculateCurrentMonthExpenses(transactions),
                dailyLimit = dailyLimit,
                totalSpentToday = calculateTodayExpenses(transactions),
                totalSavings = calculateTotalSavings(goalProgressList)
            ) as IGoalsUIState
        }.catch { e ->
            emit(IGoalsUIState.Error(R.string.error_load_goals))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IGoalsUIState.Loading
    )

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

    fun retry() { retrySignal.value++ }

    fun onGoalTitleChanged(value: String) { _titleInput.value = value }
    fun onGoalAmountChanged(value: String) { _amountInput.value = value }

    fun resetForm() {
        _formErrors.value = GoalFormErrors()
        _titleInput.value = ""
        _amountInput.value = ""
    }

    fun prepareEditForm(goal: Goal) {
        _titleInput.value = goal.title
        _amountInput.value = goal.targetAmount.toPlainString()
        _formErrors.value = GoalFormErrors()
    }

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
            submitted = true
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

    fun updateGoal(goalId: String, title: String, targetAmount: BigDecimal, deadline: LocalDate?): Boolean {
        val titleError = notEmpty(title, R.string.validation_goal_title_empty)
        val amountError = positiveAmount(targetAmount, R.string.validation_goal_amount_required)

        _formErrors.value = GoalFormErrors(
            titleErrorResId = titleError,
            amountErrorResId = amountError,
            submitted = true
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

    private fun buildGoalProgressList(
        goals: List<Goal>,
        transactions: List<Transaction>
    ): List<IGoalsUIState.GoalProgress> =
        goals.map { goal -> IGoalsUIState.GoalProgress(goal, currentAmountFor(goal, transactions)) }

    private fun currentAmountFor(goal: Goal, transactions: List<Transaction>): BigDecimal =
        transactions
            .filter { it.type == TransactionType.TRANSFER && it.goalId == goal.id }
            .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }

    private fun calculateCurrentMonthExpenses(transactions: List<Transaction>): BigDecimal {
        val currentMonth = YearMonth.now()
        return transactions
            .filter { it.type == TransactionType.EXPENSE && YearMonth.from(it.date) == currentMonth }
            .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
    }

    private fun calculateTodayExpenses(transactions: List<Transaction>): BigDecimal {
        val today = LocalDate.now()
        return transactions
            .filter { it.type == TransactionType.EXPENSE && it.date == today }
            .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
    }

    private fun calculateTotalSavings(goals: List<IGoalsUIState.GoalProgress>): BigDecimal {
        return goals.fold(BigDecimal.ZERO) { acc, g -> acc + g.currentAmount }
    }
}