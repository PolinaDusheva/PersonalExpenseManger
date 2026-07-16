package com.example.personalexpensemanager.ui.addExpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import com.example.personalexpensemanager.domain.validation.notEmpty
import com.example.personalexpensemanager.domain.validation.required
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.UUID

sealed interface GoalTransferWarning {
    data class Overflow(val remaining: BigDecimal) : GoalTransferWarning
    data object AlreadyReached : GoalTransferWarning
}

class AddExpenseViewModel(
    private val dataService: IExpenseDataService,
    private val editTransactionId: String? = null
) : ViewModel(), AddExpenseActions {

    private val _uiState = MutableStateFlow<IAddExpenseUIState>(IAddExpenseUIState.Loading)
    val uiState: StateFlow<IAddExpenseUIState> = _uiState

    private val _formState = MutableStateFlow(AddExpenseFormState(step = if (editTransactionId != null) 2 else 1))
    val formState: StateFlow<AddExpenseFormState> = _formState.asStateFlow()

    val categories: StateFlow<List<Category>> = dataService.categories
    val goals: StateFlow<List<Goal>> = dataService.goals
    private val _formErrors = MutableStateFlow(AddExpenseFormErrors())
    val formErrors: StateFlow<AddExpenseFormErrors> = _formErrors.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<Int>()
    val snackbarEvent: SharedFlow<Int> = _snackbarEvent.asSharedFlow()

    private val _showDailyLimitWarning = MutableStateFlow(false)
    val showDailyLimitWarning: StateFlow<Boolean> = _showDailyLimitWarning.asStateFlow()

    private val _showBudgetWarning = MutableStateFlow(false)
    val showBudgetWarning: StateFlow<Boolean> = _showBudgetWarning.asStateFlow()

    private val _goalWarning = MutableStateFlow<GoalTransferWarning?>(null)
    val goalWarning: StateFlow<GoalTransferWarning?> = _goalWarning.asStateFlow()

    val isEditMode: Boolean get() = editTransactionId != null

    init {
        viewModelScope.launch {
            try {
                combine(dataService.categories, dataService.goals) { c, g -> c to g }
                    .collect { (categories, goals) ->
                        val current = _uiState.value
                        if (current !is IAddExpenseUIState.Saving && current !is IAddExpenseUIState.Saved) {
                            _uiState.value = IAddExpenseUIState.Editing(categories, goals)
                        }
                    }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = IAddExpenseUIState.Error(R.string.error_load_categories)
            }
        }

        if (editTransactionId != null) {
            viewModelScope.launch {
                try {
                    dataService.getTransaction(editTransactionId)?.let { t ->
                        _formState.update {
                            it.copy(
                                title = t.title,
                                amount = t.amount.toPlainString(),
                                description = t.description,
                                date = t.date,
                                paymentMethod = t.paymentMethod,
                                transactionType = t.type,
                                selectedCategoryId = t.categoryId,
                                selectedGoalId = t.goalId
                            )
                        }
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    _uiState.value = IAddExpenseUIState.Error(R.string.error_load_edit_transaction)
                }
            }
        }

        viewModelScope.launch {
            _formState.map { it.amount }.distinctUntilChanged().debounce(300).collect { value ->
                val parsed = value.toBigDecimalOrNull()
                val error = when {
                    parsed == null || parsed == BigDecimal.ZERO -> R.string.validation_amount_required
                    parsed < BigDecimal.ZERO -> R.string.validation_amount_negative
                    else -> null
                }
                _formErrors.update { it.copy(amountErrorResId = error) }
            }
        }

        viewModelScope.launch {
            _formState.map { it.title }.distinctUntilChanged().debounce(300).collect { value ->
                _formErrors.update { it.copy(titleErrorResId = notEmpty(value, R.string.validation_title_empty)) }
            }
        }

        viewModelScope.launch {
            _formState.map { it.description }.distinctUntilChanged().debounce(300).collect { value ->
                _formErrors.update { it.copy(descriptionErrorResId = notEmpty(value, R.string.validation_description_empty)) }
            }
        }
    }

    override fun onTitleChanged(value: String) { _formState.update { it.copy(title = value) } }
    override fun onAmountChanged(value: String) { _formState.update { it.copy(amount = value) } }
    override fun onDescriptionChanged(value: String) { _formState.update { it.copy(description = value) } }

    override fun onDateSelected(date: LocalDate?) {
        _formState.update { it.copy(date = date) }
        _formErrors.update { it.copy(dateErrorResId = required(date, R.string.validation_date_required)) }
    }

    override fun onCategorySelected(categoryId: String?) {
        _formState.update { it.copy(selectedCategoryId = categoryId) }
        _formErrors.update { it.copy(categoryErrorResId = null) }
    }

    override fun onGoalSelected(goalId: String?) { _formState.update { it.copy(selectedGoalId = goalId) } }
    override fun onPaymentMethodChanged(method: PaymentMethod) { _formState.update { it.copy(paymentMethod = method) } }
    override fun onTransactionTypeChanged(type: TransactionType) { _formState.update { it.copy(transactionType = type) } }
    override fun onStepChanged(step: Int) { _formState.update { it.copy(step = step) } }

    override fun onSave() {
        val form = _formState.value
        if (!validateForm(form)) return
        if (isDailyLimitExceeded(form)) { _showDailyLimitWarning.value = true; return }
        if (isMonthlyBudgetExceeded(form)) { _showBudgetWarning.value = true; return }
        val warning = goalTransferWarning(form)
        if (warning != null) { _goalWarning.value = warning; return }
        performSave(form)
    }

    fun confirmSaveOverLimit() {
        _showDailyLimitWarning.value = false
        val form = _formState.value
        if (isMonthlyBudgetExceeded(form)) { _showBudgetWarning.value = true; return }
        performSave(form)
    }

    fun confirmSaveOverBudget() {
        _showBudgetWarning.value = false
        performSave(_formState.value)
    }
    fun dismissBudgetWarning() { _showBudgetWarning.value = false }
    fun dismissLimitWarning() { _showDailyLimitWarning.value = false }

    private fun isDailyLimitExceeded(form: AddExpenseFormState): Boolean {
        if (form.transactionType != TransactionType.EXPENSE || editTransactionId != null) return false
        if (form.date != LocalDate.now()) return false
        val dailyLimit = dataService.dailyLimit.value ?: return false
        val amount = form.amount.toBigDecimalOrNull() ?: return false

        var todayTotal = amount
        for (t in dataService.transactions.value) {
            if (t.type == TransactionType.EXPENSE && t.date == form.date) {
                todayTotal += t.amount
            }
        }
        return todayTotal > dailyLimit
    }

    private fun isMonthlyBudgetExceeded(form: AddExpenseFormState): Boolean {
        if (form.transactionType != TransactionType.EXPENSE || editTransactionId != null) return false
        val budget = dataService.monthlyBudget.value ?: return false
        val amount = form.amount.toBigDecimalOrNull() ?: return false
        val month = YearMonth.from(form.date)
        val monthTotal = dataService.transactions.value
            .filter { it.type == TransactionType.EXPENSE && YearMonth.from(it.date) == month }
            .fold(amount) { acc, t -> acc + t.amount }
        return monthTotal > budget
    }

    private fun remainingForGoal(goalId: String): BigDecimal? {
        val goal = dataService.goals.value.find { it.id == goalId } ?: return null
        val saved = dataService.transactions.value
            .filter { it.type == TransactionType.TRANSFER && it.goalId == goalId }
            .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
        return goal.targetAmount - saved
    }

    private fun goalTransferWarning(form: AddExpenseFormState): GoalTransferWarning? {
        if (form.transactionType != TransactionType.TRANSFER || editTransactionId != null) return null
        val goalId = form.selectedGoalId ?: return null
        val amount = form.amount.toBigDecimalOrNull() ?: return null
        val remaining = remainingForGoal(goalId) ?: return null
        return when {
            remaining <= BigDecimal.ZERO -> GoalTransferWarning.AlreadyReached
            amount > remaining -> GoalTransferWarning.Overflow(remaining)
            else -> null
        }
    }

    fun confirmSaveCappedToGoal() {
        val warning = _goalWarning.value
        if (warning is GoalTransferWarning.Overflow) {
            _goalWarning.value = null
            performSave(_formState.value.copy(amount = warning.remaining.toPlainString()))
        }
    }

    fun dismissGoalWarning() { _goalWarning.value = null }
    private fun performSave(form: AddExpenseFormState) {
        viewModelScope.launch {
            _uiState.value = IAddExpenseUIState.Saving
            try {
                saveTransaction(form)
                _uiState.value = IAddExpenseUIState.Saved
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = IAddExpenseUIState.Editing(
                    categories = dataService.categories.value,
                    goals = dataService.goals.value
                )
                _snackbarEvent.emit(
                    if (editTransactionId != null) R.string.error_update_transaction
                    else R.string.error_add_transaction
                )
            }
        }
    }

    private fun validateForm(form: AddExpenseFormState): Boolean {
        val amount = form.amount.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val categoryId = if (form.transactionType == TransactionType.TRANSFER) null else form.selectedCategoryId

        val titleError = notEmpty(form.title, R.string.validation_title_empty)
        val amountError = when {
            amount == BigDecimal.ZERO -> R.string.validation_amount_required
            amount < BigDecimal.ZERO -> R.string.validation_amount_negative
            else -> null
        }
        val descriptionError = notEmpty(form.description, R.string.validation_description_empty)
        val dateError = required(form.date, R.string.validation_date_required)

        val categoryError = if (form.transactionType != TransactionType.TRANSFER)
            required(categoryId, R.string.validation_category_required) else null
        val goalError = if (form.transactionType == TransactionType.TRANSFER)
            required(form.selectedGoalId, R.string.validation_goal_required) else null

        _formErrors.value = _formErrors.value.copy(
            titleErrorResId = titleError,
            amountErrorResId = amountError,
            descriptionErrorResId = descriptionError,
            dateErrorResId = dateError,
            categoryErrorResId = categoryError,
            goalErrorResId = goalError,           // ← ново
            submitted = true
        )

        return titleError == null && amountError == null && descriptionError == null
                && dateError == null && categoryError == null && goalError == null   // ← добави goalError
    }

    private suspend fun saveTransaction(form: AddExpenseFormState) {
        val amount = form.amount.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val categoryId = if (form.transactionType == TransactionType.TRANSFER) null else form.selectedCategoryId
        val goalId = if (form.transactionType == TransactionType.TRANSFER) form.selectedGoalId else null

        val transaction = Transaction(
            id = editTransactionId ?: UUID.randomUUID().toString(),
            title = form.title,
            amount = amount,
            date = form.date!!,
            currency = Currency.EUR,
            type = form.transactionType,
            categoryId = categoryId,
            description = form.description,
            paymentMethod = form.paymentMethod,
            goalId = goalId
        )

        if (editTransactionId != null) dataService.updateTransaction(transaction)
        else dataService.addTransaction(transaction)
    }
}