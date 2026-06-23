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
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.components.CategoryItem
import com.example.personalexpensemanager.ui.components.SummaryCard
import com.example.personalexpensemanager.ui.components.TransactionItem
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.time.LocalDate

@Composable
fun SuccessScreen(
    transactions: List<Transaction>,
    categories: List<Category>,
    totalAmount: Double,
    biggestExpense: Double
){
    Surface(
        modifier = Modifier.fillMaxSize(),
        //color = Color.White
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.header_height))
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
                            modifier = Modifier.height(dimensionResource(R.dimen.spacer_top)))
                        //Headline(text = "Total")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.cards_spacing))
                        ) {
                            SummaryCard(
                                title = stringResource(R.string.total_for_month),
//                                sign = '+',
                                amount = totalAmount,
                                currency = '€',
                                modifier = Modifier.weight(1f)
                            )
                            SummaryCard(
                                title = stringResource(R.string.biggest_expense),
//                                sign = '+',
                                amount = biggestExpense,
                                currency = '€',
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
                    topStart = if (isFirst) dimensionResource(R.dimen.card_corner_radius) else 0.dp,
                    topEnd = if (isFirst) dimensionResource(R.dimen.card_corner_radius) else 0.dp,
                    bottomStart = if (isLast) dimensionResource(R.dimen.card_corner_radius) else 0.dp,
                    bottomEnd = if (isLast) dimensionResource(R.dimen.card_corner_radius) else 0.dp
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
                        .height(dimensionResource(R.dimen.spacer_section)))
                    Headline(text = stringResource(R.string.categories_overview))
                }
            }

            items(
                items = categories,
                key = { "category_${it.id}" }
            ) { categoryItem ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))) {
                    CategoryItem(category = categoryItem)
                }
            }

        }
    }
}

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    DashboardContent(state.value)
}

@Composable
fun DashboardContent(state: DashboardUIState) {
    when (val s = state) {
        is DashboardUIState.Loading -> CircularProgressIndicator()
        is DashboardUIState.Success -> SuccessScreen(
            transactions = s.transactions,
            categories = s.categories,
            totalAmount = s.totalAmount,
            biggestExpense = s.biggestExpense
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
//                    Transaction(
//                        id = "t1",
//                        title = "Супермаркет",
//                        amount = 150.0,
//                        date = LocalDate.of(2026, 6, 1),
//                        currency = '€',
//                        sign = '-',
//                        categoryId = "c1"
//                    ),
//                    Transaction(
//                        id = "t2",
//                        title = "Наем",
//                        amount = 660.0,
//                        date = LocalDate.of(2026, 6, 1),
//                        currency = '€',
//                        sign = '-',
//                        categoryId = "c2"
//                    ),
//                    Transaction(
//                        id = "t3",
//                        title = "Заплата",
//                        amount = 2500.0,
//                        date = LocalDate.of(2026, 6, 5),
//                        currency = '€',
//                        sign = '+',
//                        categoryId = "c3"
//                    ),
//                    Transaction(
//                        id = "t4",
//                        title = "Интернет",
//                        amount = 20.0,
//                        date = LocalDate.of(2026, 6, 10),
//                        currency = '€',
//                        sign = '-',
//                        categoryId = "c2"
//                    ),
//                    Transaction(
//                        id = "t5",
//                        title = "Ресторант",
//                        amount = 40.0,
//                        date = LocalDate.of(2026, 6, 9),
//                        currency = '€',
//                        sign = '-',
//                        categoryId = "c1"
//                    )
//                ),
//                categories = listOf(
//                    Category(
//                        id = "c1",
//                        iconName = "restaurant",
//                        name = "Храна",
//                        progress = 0.6f,
//                        percentage = "60%"),
//                    Category(
//                        id = "c2",
//                        iconName = "home",
//                        name = "Дом",
//                        progress = 0.4f,
//                        percentage = "40%"),
//                    Category(
//                        id = "c3",
//                        iconName = "payments",
//                        name = "Сметки",
//                        progress = 0.1f,
//                        percentage = "10%")
//                ),
//                totalAmount = 2500.0,
//                biggestExpense = 660.0
//            )
//        )
//    }
//}
