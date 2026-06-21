package com.example.personalexpensemanager.ui.transaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.navigation.Screen
import com.example.personalexpensemanager.ui.dashboard.DashboardContent
import com.example.personalexpensemanager.ui.dashboard.DashboardUIState
import com.example.personalexpensemanager.ui.dashboard.DashboardViewModel

@Composable
fun SuccessScreen(
    transactions: List<Transaction>,

) {
//    TopAppBar(
//        title = { Text(stringResource(R.string.transaction_history)) },
//        navigationIcon = {
//            IconButton(onClick = onBack) {
//                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
//            }
//        },
//        actions = {
//            IconButton(onClick = onAddExpense) {
//                Icon(Icons.Outlined.Add, contentDescription = null)
//            }
//        }
//    )
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        FilterChip(
//            selected = selectedCategory == null,
//            onClick = { onFilterByCategory(null) },
//            label = { Text("All Categories") }
//        )
//        FilterChip(
//            selected = sortedByDate,
//            onClick = { onSortByDate() },
//            label = { Text("Sort by date") }
//        )
//        FilterChip(
//            selected = sortedByAmount,
//            onClick = { onSortByAmount() },
//            label = { Text("Sort by amount") }
//        )
//    }
//    Box(
//        modifier = Modifier
//            .size(44.dp)
//            .background(
//                color = if (transaction.sign == '-') Color(0xFFFFE5E5) else Color(0xFFEDE7FF),
//                shape = CircleShape
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = if (transaction.sign == '-')
//                Icons.Outlined.ArrowOutward      // стрелка нагоре-вдясно
//            else
//                Icons.Outlined.ArrowInward,      // стрелка надолу-вляво
//            tint = if (transaction.sign == '-') Color.Red else Color(0xFF6650A4)
//        )
//    }

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
            transactions = s.transactions,
            categories = s.categories,
        )
        is TransactionUIState.Error   -> Text(s.message)
    }
}