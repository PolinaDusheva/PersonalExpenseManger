package com.example.personalexpensemanager.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.ui.components.CategoryDropdown
import com.example.personalexpensemanager.ui.components.TransactionItem
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(
    state: TransactionUIState.Success,
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
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(R.dimen.padding_horizontal),
                            vertical = dimensionResource(R.dimen.padding_small)
                        ),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                ) {
                    CategoryDropdown(
                        categories = state.categories,
                        selectedCategoryId = state.selectedCategory,
                        onCategorySelected = onCategorySelected,
                        includeAll = true
                    )
                    FilterChip(
                        selected = state.sortingType == SortingType.AMOUNT,
                        onClick = { onSortSelected(SortingType.AMOUNT) },
                        label = { Text(stringResource(R.string.sort_by_amount)) }
                    )
                    FilterChip(
                        selected = state.sortingType == SortingType.DATE,
                        onClick = { onSortSelected(SortingType.DATE) },
                        label = { Text(stringResource(R.string.sort_by_date)) }
                    )
                }
            }

//            state.groupedByDate.forEach { (date, transactionsForDate) ->
//                item(key = "header_$date") {
//                    Text(
//                        text = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 14.sp,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        modifier = Modifier.padding(
//                            horizontal = dimensionResource(R.dimen.padding_horizontal),
//                            vertical = dimensionResource(R.dimen.padding_small)
//                        )
//                    )
//                }
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
//            }
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
        onCategorySelected = viewModel::filterByCategory,
        onSortSelected = viewModel::sortBy,
        onTransactionClick = onTransactionClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsContent(
    state: TransactionUIState,
    onBack: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onCategorySelected: (String?) -> Unit = {},
    onSortSelected: (SortingType) -> Unit = {},
    onTransactionClick: (String) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        TopAppBar(
            title = {
                Text(
                    stringResource(R.string.transaction_history),
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
        when (val s = state) {
            is TransactionUIState.Loading -> CircularProgressIndicator()
            is TransactionUIState.Success -> SuccessScreen(
                state = s,
                onRefresh = onRefresh,
                onCategorySelected = onCategorySelected,
                onSortSelected = onSortSelected,
                onTransactionClick = onTransactionClick
            )
            is TransactionUIState.Error -> Text(s.message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun TransactionScreenPreview() {
    PersonalExpenseManagerTheme {
        val list = listOf(
            Transaction(id = "1", title = "Супермаркет", amount = 150.00, date = LocalDate.of(2026, 11, 6), currency = '€', sign = '-', categoryId = "1", description = "Седмично пазаруване", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "2", title = "Наем", amount = 660.00, date = LocalDate.of(2026, 6, 1), currency = '€', sign = '-', categoryId = "1", description = "Месечен наем", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "3", title = "Заплата", amount = 2500.00, date = LocalDate.of(2026, 6, 5), currency = '€', sign = '+', categoryId = "1", description = "Месечна заплата", paymentMethod = PaymentMethod.CARD)
        )
        TransactionsContent(
            state = TransactionUIState.Success(
                transactions = list,
                filteredTransactions = list,
                categories = listOf(
                    Category("1", "restaurant", "Храна", 0.6f, "60%"),
                    Category("2", "car", "Транспорт", 0.3f, "30%")
                ),
                selectedCategory = null,
                sortingType = SortingType.NONE
            )
        )
    }
}