package com.example.personalexpensemanager.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import com.example.personalexpensemanager.ui.statistics.components.Period

class TransactionViewModel(
    private val dataService: IExpenseDataService
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _sortingType = MutableStateFlow(SortingType.NONE)
    private val _selectedPeriod = MutableStateFlow<Period?>(null)

    private val retrySignal = MutableStateFlow(0)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val uiState: StateFlow<ITransactionUIState> = retrySignal.flatMapLatest {
        combine(
            dataService.transactions,
            dataService.categories,
            _selectedCategory,
            _sortingType,
            _selectedPeriod
        ) { transactions, categories, selectedCategory, sortingType, period ->
            val reversedTransactions = transactions.reversed()

            var filtered = reversedTransactions
            if (selectedCategory != null) {
                filtered = filtered.filter { it.categoryId == selectedCategory }
            }
            if (period != null) {
                filtered = filtered.filter { it.date in period.start..period.end }
            }

            val sorted = applySorting(filtered, sortingType)
            ITransactionUIState.Success(
                transactions = reversedTransactions,
                filteredTransactions = sorted,
                categories = categories,
                categoriesById = categories.associateBy { it.id },
                selectedCategory = selectedCategory,
                sortingType = sortingType,
                selectedPeriod = period
            ) as ITransactionUIState
        }.catch { e ->
            emit(ITransactionUIState.Error(R.string.error_load_transactions))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ITransactionUIState.Loading
    )

    fun retry() { retrySignal.value++ }

    fun filterByCategory(categoryId: String?) {
        _selectedCategory.value = categoryId
    }

    fun sortBy(sortingType: SortingType) {
        _sortingType.value = if (_sortingType.value == sortingType) SortingType.NONE else sortingType
    }

    fun filterByPeriod(period: Period?) {
        _selectedPeriod.value = period
    }

    private fun applySorting(transactions: List<Transaction>, sortingType: SortingType): List<Transaction> {
        return when (sortingType) {
            SortingType.AMOUNT -> transactions.sortedByDescending { it.amount }
            SortingType.DATE -> transactions.sortedByDescending { it.date }
            SortingType.NONE -> transactions
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
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}