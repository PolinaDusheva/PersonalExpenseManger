package com.example.personalexpensemanager.ui.transaction

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import com.example.personalexpensemanager.ui.components.CategoryDropdown
import com.example.personalexpensemanager.ui.components.ErrorScreen
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.components.TransactionItem
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.personalexpensemanager.ui.components.TransactionFilterChip
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SuccessScreen(
    state: ITransactionUIState.Success,
    onRefresh: () -> Unit,
    onCategorySelected: (String?) -> Unit,
    onSortSelected: (SortingType) -> Unit,
    onTransactionClick: (String) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(
                            horizontal = dimensionResource(R.dimen.padding_horizontal),
                            vertical = dimensionResource(R.dimen.padding_small)
                        ),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                ) {
                    CategoryDropdown(
                        categories = state.categories,
                        selectedCategory = state.categories.find { it.id == state.selectedCategory },
                        onCategorySelected = { category -> onCategorySelected(category?.id) },
                        includeAll = true
                    )
                    TransactionFilterChip(
                        text = stringResource(R.string.sort_by_amount),
                        selected = state.sortingType == SortingType.AMOUNT,
                        onClick = { onSortSelected(SortingType.AMOUNT) }
                    )
                    TransactionFilterChip(
                        text = stringResource(R.string.sort_by_date),
                        selected = state.sortingType == SortingType.DATE,
                        onClick = { onSortSelected(SortingType.DATE) }
                    )
                }
            }

            val grouped = state.groupedByDate
            if (grouped != null) {
                grouped.forEach { (date, transactionsForDate) ->
                    item(key = "header_$date") {
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern(stringResource(R.string.date_format_pattern))),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = dimensionResource(R.dimen.padding_horizontal),
                                    vertical = dimensionResource(R.dimen.padding_small)
                                )
                        )
                    }
                    items(
                        items = transactionsForDate,
                        key = { it.id }
                    ) { transaction ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onTransactionClick(transaction.id) }
                                .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                        ) {
                            TransactionItem(transaction)
                        }
                    }
                }
            } else {
                items(
                    items = state.filteredTransactions,
                    key = { it.id }
                ) { transaction ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTransactionClick(transaction.id) }
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                    ) {
                        TransactionItem(transaction)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionsScreen(
    viewModel: TransactionViewModel,
    onBack: () -> Unit = {},
    onTransactionClick: (String) -> Unit = {}
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    TransactionsContent(
        state = state.value,
        onBack = onBack,
        onRefresh = viewModel::refresh,
        onRetry = viewModel::retry,
        onCategorySelected = viewModel::filterByCategory,
        onSortSelected = viewModel::sortBy,
        onTransactionClick = onTransactionClick
    )
}

@Composable
fun TransactionsContent(
    state: ITransactionUIState,
    onBack: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onRetry: () -> Unit = {},
    onCategorySelected: (String?) -> Unit = {},
    onSortSelected: (SortingType) -> Unit = {},
    onTransactionClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(top = dimensionResource(R.dimen.padding_small))
    ) {
        Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal))) {
            Headline(text = stringResource(R.string.transaction_history))
        }
        when (state)  {
            is ITransactionUIState.Loading -> CircularProgressIndicator()
            is ITransactionUIState.Success -> SuccessScreen(
                state = state,
                onRefresh = onRefresh,
                onCategorySelected = onCategorySelected,
                onSortSelected = onSortSelected,
                onTransactionClick = onTransactionClick
            )
            is ITransactionUIState.Error -> ErrorScreen(
                messageResId = state.messageResId,
                onRetry = onRetry
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, name = "Без групиране")
fun TransactionScreenPreview() {
    PersonalExpenseManagerTheme {
        val list = listOf(
            Transaction(id = "1", title = "Супермаркет", amount = BigDecimal("150.00"), date = LocalDate.of(2026, 6, 6), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "1", description = "Седмично пазаруване", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "2", title = "Наем", amount = BigDecimal("660.00"), date = LocalDate.of(2026, 6, 1), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "1", description = "Месечен наем", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "3", title = "Заплата", amount = BigDecimal("2500.00"), date = LocalDate.of(2026, 6, 5), currency = Currency.EUR, type = TransactionType.INCOME, categoryId = "1", description = "Месечна заплата", paymentMethod = PaymentMethod.CARD)
        )
        TransactionsContent(
            state = ITransactionUIState.Success(
                transactions = list,
                filteredTransactions = list,
                categories = listOf(
                    Category("1", "food", "Храна"),
                    Category("2", "transport", "Транспорт")
                ),
                selectedCategory = null,
                sortingType = SortingType.NONE
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, name = "Групирано по дата")
fun TransactionScreenGroupedPreview() {
    PersonalExpenseManagerTheme {
        val list = listOf(
            Transaction(id = "1", title = "Супермаркет", amount = BigDecimal("150.00"), date = LocalDate.of(2026, 6, 6), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "1", description = "Седмично пазаруване", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "2", title = "Кафе", amount = BigDecimal("8.00"), date = LocalDate.of(2026, 6, 6), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "1", description = "Сутрешно кафе", paymentMethod = PaymentMethod.CASH),
            Transaction(id = "3", title = "Заплата", amount = BigDecimal("2500.00"), date = LocalDate.of(2026, 6, 5), currency = Currency.EUR, type = TransactionType.INCOME, categoryId = "1", description = "Месечна заплата", paymentMethod = PaymentMethod.CARD)
        )
        TransactionsContent(
            state = ITransactionUIState.Success(
                transactions = list,
                filteredTransactions = list,
                categories = listOf(
                    Category("1", "food", "Храна"),
                    Category("2", "transport", "Транспорт")
                ),
                selectedCategory = null,
                sortingType = SortingType.DATE
            )
        )
    }
}