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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.MockDataService
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.components.CategoryItem
import com.example.personalexpensemanager.ui.components.SummaryCard
import com.example.personalexpensemanager.ui.components.TransactionItem

@Composable
fun SuccessScreen(
    transactions: List<Transaction>,
    categories: List<Category>
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
                                sign = '+',
                                amount = "120 000",
                                currency = '€',
                                modifier = Modifier.weight(1f)
                            )
                            SummaryCard(
                                title = stringResource(R.string.biggest_expense),
                                sign = '+',
                                amount = "120 000",
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

//            items(
//                items = transactions,
//                key = { it.id }
//            ) { transaction ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(
//                            horizontal = dimensionResource(R.dimen.padding_horizontal),
//                            vertical = dimensionResource(R.dimen.card_vertical_spacing)
//                        )
//                        .shadow(
//                            elevation = dimensionResource(R.dimen.card_elevation),
//                            shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
//                            clip = false,
//                            ambientColor = Color.Black.copy(alpha = 0.5f),
//                            spotColor = Color.Black.copy(alpha = 0.5f)
//                        ),
//                    colors = CardDefaults.cardColors(containerColor = Color.White)
//                ) {
//                    Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_standard))) {
//                        TransactionItem(transaction)
//                    }
//                }
//            }
            items(
                items = transactions,
                key = { it.id }
            ) { transaction ->
                val isFirst = transaction.id == transactions.first().id
                val isLast = transaction.id == transactions.last().id
                val shape = RoundedCornerShape(
                    topStart = if (isFirst) dimensionResource(R.dimen.card_corner_radius) else 0.dp,
                    topEnd = if (isFirst) dimensionResource(R.dimen.card_corner_radius) else 0.dp,
                    bottomStart = if (isLast) dimensionResource(R.dimen.card_corner_radius) else 0.dp,
                    bottomEnd = if (isLast) dimensionResource(R.dimen.card_corner_radius) else 0.dp
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                        .fillMaxWidth()
                        .background(Color.White, shape)
                        .padding(horizontal = dimensionResource(R.dimen.padding_standard))
                ) {
                    TransactionItem(transaction)
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
                key = { it.id }
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
fun DashboardScreen(
    viewModel: DashboardViewModel
) {
    val state  = viewModel.uiState.collectAsStateWithLifecycle()
    when(val s = state.value){
        is DashboardUIState.Loading -> CircularProgressIndicator()
        is DashboardUIState.Success -> SuccessScreen(s.transactions, s.categories)
        is DashboardUIState.Error   -> Text(s.message)
    }


}

//@Preview(showBackground = true)
//@Composable
//fun DashboardPreview() {
//    MaterialTheme {
//        val dataService = MockDataService()
//        DashboardScreen(
//            viewModel =
////            transactions = dataService.getTransaction(),
////            categories = dataService.getCategories()
//        )
//    }
//}
