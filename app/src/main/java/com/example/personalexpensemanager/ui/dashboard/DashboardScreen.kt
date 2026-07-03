package com.example.personalexpensemanager.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.FakeExpenseDataService
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.components.CategoryItem
import com.example.personalexpensemanager.ui.components.SummaryCard
import com.example.personalexpensemanager.ui.components.TransactionItem
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.time.LocalDate

@Composable
fun SuccessScreen(
    transactions: List<Transaction>,
    categoriesMap: HashMap<Category, Float>,
    totalAmount: Double,
    biggestExpense: Double,
    onRefresh: () -> Unit
){
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item{
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
                                modifier = Modifier.height(dimensionResource(R.dimen.dashboard_spacer_top)))
                            //Headline(text = "Total")
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dashboard_cards_spacing))
                            ) {
                                SummaryCard(
                                    title = stringResource(R.string.total_for_month),
                                    amount = totalAmount,
                                    currency = Currency.EUR,
                                    modifier = Modifier.weight(1f)
                                )
                                SummaryCard(
                                    title = stringResource(R.string.biggest_expense),
                                    amount = biggestExpense,
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
                                .height(dimensionResource(R.dimen.padding_small)))
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
                        topStart = if (isFirst) dimensionResource(R.dimen.transaction_card_corner_radius) else 0.dp,
                        topEnd = if (isFirst) dimensionResource(R.dimen.transaction_card_corner_radius) else 0.dp,
                        bottomStart = if (isLast) dimensionResource(R.dimen.transaction_card_corner_radius) else 0.dp,
                        bottomEnd = if (isLast) dimensionResource(R.dimen.transaction_card_corner_radius) else 0.dp
                    )
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                            .fillMaxWidth(),
                        shape = shape,
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = dimensionResource(R.dimen.padding_standard),

                                )
                        ) {
                            TransactionItem(transaction)
                        }
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                    ) {
                        Spacer(modifier = Modifier
                            .height(dimensionResource(R.dimen.dashboard_section_spacer)))
                        Headline(text = stringResource(R.string.categories_overview))
                    }
                }
                val categoriesEntries = categoriesMap.entries.toList()
                items(
                    items = categoriesEntries,
                    key = { "category_${it.key.id}" }
                ) { categoryEntry ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))) {
                        CategoryItem(
                            category =categoryEntry.key,
                            categorySize =categoryEntry.value
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    DashboardContent(
        state.value,
        onRefresh = viewModel::refresh
        )
}

@Composable
fun DashboardContent(
    state: DashboardUIState,
    onRefresh: () -> Unit = {}) {
    when (val s = state) {
        is DashboardUIState.Loading -> CircularProgressIndicator()
        is DashboardUIState.Success -> SuccessScreen(
            transactions = s.transactions,
            categoriesMap = s.categoriesMap,
            totalAmount = s.totalAmount,
            biggestExpense = s.biggestExpense,
            onRefresh = onRefresh
        )
        is DashboardUIState.Error   -> Text(s.message)
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DashboardPreview() {
//    PersonalExpenseManagerTheme {
//        DashboardContent(
//            state = DashboardUIState.Success(
//                transactions = listOf(
//                    Transaction("1", "Супермаркет", 150.0, LocalDate.of(2026, 6, 1), '€', '-', "c1", "Пазаруване", PaymentMethod.CARD),
//                    Transaction("2", "Заплата", 2500.0, LocalDate.of(2026, 6, 5), '€', '+', "c1", "Месечна заплата", PaymentMethod.CARD),
//                ),
//                categories = listOf(
//                    Category("c1", "restaurant", "Храна", 0.6f, "60%"),
//                    Category("c2", "car", "Транспорт", 0.3f, "30%")
//                ),
//                totalAmount = 2500.0,
//                biggestExpense = 660.0
//            )
//        )
//    }
//}
