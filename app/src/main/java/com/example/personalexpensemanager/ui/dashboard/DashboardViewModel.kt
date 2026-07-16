package com.example.personalexpensemanager.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.TransactionHelper
import com.example.personalexpensemanager.domain.TransactionHelper.calculateTotalExpenses
import com.example.personalexpensemanager.domain.enums.TransactionType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.coroutines.cancellation.CancellationException

class DashboardViewModel(
    private val dataService: IExpenseDataService
) : ViewModel() {

    private val retrySignal = MutableStateFlow(0)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<Int>()
    val snackbarEvent: SharedFlow<Int> = _snackbarEvent.asSharedFlow()

    val uiState: StateFlow<IDashboardUIState> = retrySignal.flatMapLatest {
        combine(
            dataService.transactions,
            dataService.categories
        ) { transactions, categories ->
            if (transactions.isEmpty() && categories.isEmpty()) {
                IDashboardUIState.Empty
            } else {
                val currentMonthTransactions = TransactionHelper.filterCurrentMonth(transactions)
                IDashboardUIState.Success(
                    transactions = transactions.takeLast(RECENT_TRANSACTIONS_COUNT).reversed(),
                    categoriesMap = calculateCategoriesPercentage(categories, currentMonthTransactions),
                    categoriesById = categories.associateBy { it.id },
                    totalAmount = TransactionHelper.calculateTotalExpenses(currentMonthTransactions),
                    biggestExpense = TransactionHelper.calculateBiggestExpense(currentMonthTransactions)
                )
            }
        }.catch { e ->
            emit(IDashboardUIState.Error(R.string.error_load_dashboard))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IDashboardUIState.Loading
    )

    fun retry() { retrySignal.value++ }



    private fun calculateCategoriesPercentage(
        categories: List<Category>,
        transactions: List<Transaction>
    ): Map<Category, Float> {
        val totalExpensesAmount = calculateTotalExpenses(transactions)
        return categories.associateWith { category ->
            val categoryTotalAmount = transactions
                .filter { it.categoryId == category.id && it.type == TransactionType.EXPENSE }
                .fold(0f) { acc, t -> acc + t.amount.toFloat() }
            if (totalExpensesAmount > BigDecimal.ZERO) {
                categoryTotalAmount / totalExpensesAmount.toFloat()
            } else {
                0f
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                dataService.getTransactions()
                dataService.getCategories()
                retrySignal.value++
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
            _snackbarEvent.emit(R.string.error_load_dashboard)
        } finally {
                _isRefreshing.value = false
            }
        }
    }

    companion object {
        private const val RECENT_TRANSACTIONS_COUNT = 5
    }
}