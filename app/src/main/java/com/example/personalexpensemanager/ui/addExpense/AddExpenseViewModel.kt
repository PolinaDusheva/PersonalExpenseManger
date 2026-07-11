package com.example.personalexpensemanager.ui.addExpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import com.example.personalexpensemanager.domain.validation.notEmpty
import com.example.personalexpensemanager.domain.validation.required
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

class AddExpenseViewModel(
    private val dataService: IExpenseDataService,
    private val editTransactionId: String? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow<IAddExpenseUIState>(IAddExpenseUIState.Loading)
    val uiState: StateFlow<IAddExpenseUIState> = _uiState

    private val _formErrors = MutableStateFlow(AddExpenseFormErrors())
    val formErrors: StateFlow<AddExpenseFormErrors> = _formErrors.asStateFlow()

    private val _amountInput = MutableStateFlow("")
    private val _titleInput = MutableStateFlow("")
    private val _descriptionInput = MutableStateFlow("")
    private val _existingTransaction = MutableStateFlow<Transaction?>(null)
    val existingTransaction: StateFlow<Transaction?> = _existingTransaction.asStateFlow()

    val isEditMode: Boolean get() = editTransactionId != null

    init {
        viewModelScope.launch {
            try {
                combine(dataService.categories, dataService.goals) { categories, goals ->
                    categories to goals
                }.collect { (categories, goals) ->
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
                try{
                    val transaction = dataService.getTransaction(editTransactionId)
                    if (transaction != null) {
                        _existingTransaction.value = transaction
                        _amountInput.value = transaction.amount.toPlainString()
                        _titleInput.value = transaction.title
                        _descriptionInput.value = transaction.description
                    }
                }catch(e: CancellationException){
                    throw e
                }catch (e: Exception){
                    _uiState.value = IAddExpenseUIState.Error(R.string.error_load_edit_transaction)
                }

            }
        }

        viewModelScope.launch {
            _amountInput.debounce(300).collect { value ->
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
            _titleInput.debounce(300).collect { value ->
                val error = notEmpty(value, R.string.validation_title_empty)
                _formErrors.update { it.copy(titleErrorResId = error) }
            }
        }

        viewModelScope.launch {
            _descriptionInput.debounce(300).collect { value ->
                val error = notEmpty(value, R.string.validation_description_empty)
                _formErrors.update { it.copy(descriptionErrorResId = error) }
            }
        }
    }

    fun onAmountChanged(value: String) { _amountInput.value = value }
    fun onAmountTouched() { _formErrors.update { it.copy(amountTouched = true) } }
    fun onTitleChanged(value: String) { _titleInput.value = value }
    fun onTitleTouched() { _formErrors.update { it.copy(titleTouched = true) } }
    fun onDescriptionChanged(value: String) { _descriptionInput.value = value }
    fun onDescriptionTouched() { _formErrors.update { it.copy(descriptionTouched = true) } }
    fun onDateSelected(date: LocalDate?) {
        val error = required(date, R.string.validation_date_required)
        _formErrors.update { it.copy(dateErrorResId = error, dateTouched = true) }
    }
    fun onCategorySelected() {
        _formErrors.update { it.copy(categoryErrorResId = null, categoryTouched = true) }
    }

    fun addExpense(
        title: String,
        amount: BigDecimal,
        categoryId: String?,
        date: LocalDate?,
        description: String,
        paymentMethod: PaymentMethod,
        transactionType: TransactionType,
        goalId: String? = null
    ) {
        val amountError = when {
            amount == BigDecimal.ZERO -> R.string.validation_amount_required
            amount < BigDecimal.ZERO -> R.string.validation_amount_negative
            else -> null
        }
        val titleError = notEmpty(title, R.string.validation_title_empty)
        val descriptionError = notEmpty(description, R.string.validation_description_empty)
        val dateError = required(date, R.string.validation_date_required)
        val categoryError = if (transactionType != TransactionType.TRANSFER) {
            required(categoryId, R.string.validation_category_required)
        } else null

        _formErrors.value = _formErrors.value.copy(
            titleErrorResId = titleError,
            amountErrorResId = amountError,
            descriptionErrorResId = descriptionError,
            dateErrorResId = dateError,
            categoryErrorResId = categoryError,
            titleTouched = true,
            amountTouched = true,
            descriptionTouched = true,
            dateTouched = true,
            categoryTouched = true
        )

        if (titleError != null
            || amountError != null
            || descriptionError != null
            || dateError != null
            || categoryError != null) {
            return
        }

        viewModelScope.launch {
            _uiState.value = IAddExpenseUIState.Saving
            try {
                val transaction = Transaction(
                    id = editTransactionId ?: UUID.randomUUID().toString(),
                    title = title,
                    amount = amount,
                    date = date!!,
                    currency = Currency.EUR,
                    type = transactionType,
                    categoryId = categoryId,
                    description = description,
                    paymentMethod = paymentMethod,
                    goalId = goalId
                )
                if (editTransactionId != null) {
                    dataService.updateTransaction(transaction)
                } else {
                    dataService.addTransaction(transaction)
                }
                _uiState.value = IAddExpenseUIState.Saved
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = if (editTransactionId != null)
                    IAddExpenseUIState.Error(R.string.error_update_transaction)
                else
                    IAddExpenseUIState.Error(R.string.error_add_transaction)
            }
        }
    }
}