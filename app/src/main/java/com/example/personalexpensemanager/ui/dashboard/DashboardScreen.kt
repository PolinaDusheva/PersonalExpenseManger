package com.example.personalexpensemanager.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import com.example.personalexpensemanager.ui.components.AppSnackbarHost
import com.example.personalexpensemanager.ui.components.CategoryItem
import com.example.personalexpensemanager.ui.components.ErrorScreen
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.components.SummaryCard
import com.example.personalexpensemanager.ui.components.TransactionItem
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import com.example.personalexpensemanager.ui.theme.TextSecondary
import java.math.BigDecimal
import java.time.LocalDate

private val NO_CORNER_RADIUS = 0.dp

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onTransactionClick: (String) -> Unit = {}
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing = viewModel.isRefreshing.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { resId ->
            snackbarHostState.showSnackbar(context.getString(resId))
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            DashboardContent(
                state.value,
                isRefreshing = isRefreshing.value,
                onRefresh = viewModel::refresh,
                onRetry = viewModel::retry,
                onTransactionClick = onTransactionClick
            )
        }
    }
}

@Composable
fun DashboardContent(
    state: IDashboardUIState,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onRetry: () -> Unit = {},
    onTransactionClick: (String) -> Unit = {}
) {
    when (state) {
        is IDashboardUIState.Loading -> CircularProgressIndicator()
        is IDashboardUIState.Success -> SuccessScreen(
            transactions = state.transactions,
            categoriesMap = state.categoriesMap,
            categoriesById = state.categoriesById,
            totalAmount = state.totalAmount,
            biggestExpense = state.biggestExpense,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            onTransactionClick = onTransactionClick
        )
        is IDashboardUIState.Empty -> EmptyScreen(isRefreshing = isRefreshing, onRefresh = onRefresh)
        is IDashboardUIState.Error -> ErrorScreen(
            messageResId = state.messageResId,
            onRetry = onRetry
        )
    }
}

@Composable
fun EmptyScreen(
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.dashboard_header_height))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.waves_bg),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            alpha = 1f
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    horizontal = dimensionResource(R.dimen.padding_horizontal),
                                    vertical = dimensionResource(R.dimen.padding_horizontal)
                                )
                        ) {
                            Spacer(
                                modifier = Modifier.height(dimensionResource(R.dimen.dashboard_spacer_top))
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dashboard_cards_spacing))
                            ) {
                                SummaryCard(
                                    title = stringResource(R.string.total_for_month),
                                    amount = BigDecimal.ZERO,
                                    currency = Currency.EUR,
                                    modifier = Modifier.weight(1f)
                                )
                                SummaryCard(
                                    title = stringResource(R.string.biggest_expense),
                                    amount = BigDecimal.ZERO,
                                    currency = Currency.EUR,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(R.dimen.padding_small))
                        )
                        Headline(text = stringResource(R.string.recent_transactions))
                    }
                }

                item {
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.dashboard_empty_card_height)),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.transaction_card_corner_radius)),
                        color = Color.White,
                        shadowElevation = dimensionResource(R.dimen.elevation)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.dashboard_no_transactions),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(R.dimen.dashboard_section_spacer))
                        )
                        Headline(text = stringResource(R.string.categories_overview))
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.dashboard_no_categories),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensionResource(R.dimen.padding_horizontal),
                                vertical = dimensionResource(R.dimen.padding_standard)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun SuccessScreen(
    transactions: List<Transaction>,
    categoriesMap: Map<Category, Float>,
    categoriesById: Map<String, Category>,
    totalAmount: BigDecimal,
    biggestExpense: BigDecimal,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit,
    onTransactionClick: (String) -> Unit = {}
) {
    val currency = transactions.firstOrNull()?.currency ?: Currency.EUR
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.dashboard_header_height))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.waves_bg),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            alpha = 1f
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    horizontal = dimensionResource(R.dimen.padding_horizontal),
                                    vertical = dimensionResource(R.dimen.padding_horizontal)
                                )
                        ) {
                            Spacer(
                                modifier = Modifier.height(dimensionResource(R.dimen.dashboard_spacer_top))
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dashboard_cards_spacing))
                            ) {
                                SummaryCard(
                                    title = stringResource(R.string.total_for_month),
                                    amount = totalAmount,
                                    currency = currency,
                                    highlighted = true,
                                    modifier = Modifier.weight(1f)
                                )
                                SummaryCard(
                                    title = stringResource(R.string.biggest_expense),
                                    amount = biggestExpense,
                                    currency = currency,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(R.dimen.padding_small))
                        )
                        Headline(text = stringResource(R.string.recent_transactions))
                    }
                }

                items(
                    items = transactions,
                    key = { "transaction_${it.id}" }
                ) { transaction ->
                    val isFirst = transaction.id == transactions.first().id
                    val isLast = transaction.id == transactions.last().id
                    val shape = RoundedCornerShape(
                        topStart = if (isFirst) dimensionResource(R.dimen.transaction_card_corner_radius) else NO_CORNER_RADIUS,
                        topEnd = if (isFirst) dimensionResource(R.dimen.transaction_card_corner_radius) else NO_CORNER_RADIUS,
                        bottomStart = if (isLast) dimensionResource(R.dimen.transaction_card_corner_radius) else NO_CORNER_RADIUS,
                        bottomEnd = if (isLast) dimensionResource(R.dimen.transaction_card_corner_radius) else NO_CORNER_RADIUS
                    )
                    Surface(
                        onClick = { onTransactionClick(transaction.id) },
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                            .fillMaxWidth(),
                        shape = shape,
                        color = Color.White,
                        shadowElevation = dimensionResource(R.dimen.elevation)
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = dimensionResource(R.dimen.padding_standard),
                            )
                        ) {
                            TransactionItem(
                                transaction = transaction,
                                category = categoriesById[transaction.categoryId]
                            )
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(R.dimen.dashboard_section_spacer))
                        )
                        Headline(text = stringResource(R.string.categories_overview))
                    }
                }

                val categoriesEntries = categoriesMap.entries.toList()
                if (categoriesEntries.isEmpty()) {
                    item {
                        Surface(
                            modifier = Modifier
                                .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.dashboard_empty_card_height)),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.transaction_card_corner_radius)),
                            color = Color.White,
                            shadowElevation = dimensionResource(R.dimen.elevation)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.dashboard_no_categories),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                } else {
                    items(
                        items = categoriesEntries,
                        key = { "category_${it.key.id}" }
                    ) { categoryEntry ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                        ) {
                            CategoryItem(
                                category = categoryEntry.key,
                                categorySize = categoryEntry.value
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Dashboard with data")
@Composable
fun DashboardPreview() {
    PersonalExpenseManagerTheme {
        DashboardContent(
            state = IDashboardUIState.Success(
                transactions = listOf(
                    Transaction("1", "Супермаркет", BigDecimal("150.00"), LocalDate.of(2026, 7, 5), Currency.EUR, TransactionType.EXPENSE, "1", "Пазаруване", PaymentMethod.CARD),
                    Transaction("2", "Заплата", BigDecimal("2500.00"), LocalDate.of(2026, 7, 5), Currency.EUR, TransactionType.INCOME, "1", "Месечна заплата", PaymentMethod.CARD)
                ),
                categoriesMap = hashMapOf(
                    Category("1", "food", "Храна") to 0.6f,
                    Category("2", "transport", "Транспорт") to 0.4f
                ),
                categoriesById = mapOf(
                    "1" to Category("1", "food", "Храна"),
                    "2" to Category("2", "transport", "Транспорт")
                ),
                totalAmount = BigDecimal("150.00"),
                biggestExpense = BigDecimal("150.00")
            )
        )
    }
}

@Preview(showBackground = true, name = "Dashboard empty")
@Composable
fun DashboardEmptyPreview() {
    PersonalExpenseManagerTheme {
        DashboardContent(state = IDashboardUIState.Empty)
    }
}