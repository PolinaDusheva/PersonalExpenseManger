package com.example.personalexpensemanager.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.navigation.Screen
import com.example.personalexpensemanager.ui.components.TransactionItem
import com.example.personalexpensemanager.ui.dashboard.DashboardContent
import com.example.personalexpensemanager.ui.dashboard.DashboardUIState
import com.example.personalexpensemanager.ui.dashboard.DashboardViewModel

@Composable
fun SuccessScreen(
    filteredTransactions: List<Transaction>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = filteredTransactions,
            key = { it.id }
        ) { transaction ->
            val isFirst = transaction.id == filteredTransactions.first().id
            val isLast = transaction.id == filteredTransactions.last().id
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
    }


}
@Composable
fun TransactionsScreen(viewModel: TransactionViewModel) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    TransactionsContent(state.value)
}

@Composable
fun TransactionsContent(state: TransactionUIState) {
    when (val s = state) {
        is TransactionUIState.Loading -> CircularProgressIndicator()
        is TransactionUIState.Success -> SuccessScreen(
            filteredTransactions = s.filteredTransactions,
            //categories = s.categories,
        )
        is TransactionUIState.Error   -> Text(s.message)
    }
}

//@Composable
//@Preview(showBackground = true)
//fun TransactionScreenPreview(){
//    SuccessScreen()
//}