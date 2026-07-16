package com.example.personalexpensemanager.ui.transactionDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.TransactionType
import com.example.personalexpensemanager.ui.components.ErrorScreen
import com.example.personalexpensemanager.ui.components.appButtons.PrimaryButton
import com.example.personalexpensemanager.ui.transactionDetails.components.TransactionDetailsHeader
import com.example.personalexpensemanager.ui.transactionDetails.components.TransactionInfoCard
import java.time.format.DateTimeFormatter
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.ui.components.appButtons.DialogConfirmButton
import com.example.personalexpensemanager.ui.components.appButtons.DialogDismissButton
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.math.BigDecimal
import java.time.LocalDate
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalContext
import com.example.personalexpensemanager.ui.components.AppSnackbarHost

@Composable
fun TransactionDetailsScreen(
    viewModel: TransactionDetailsViewModel,
    onClose: () -> Unit,
    onEdit: (String) -> Unit,
    onShare: (Transaction) -> Unit = {}
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val deleted by viewModel.deleted.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(deleted) {
        if (deleted) onClose()
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { resId ->
            snackbarHostState.showSnackbar(context.getString(resId))
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            TransactionDetailsScreenContent(
                state = state.value,
                onClose = onClose,
                onEdit = onEdit,
                onDelete = { viewModel.deleteTransaction() },
                onShare = onShare,
                onRetry = { viewModel.retry() }
            )
        }
    }
}

@Composable
fun TransactionDetailsScreenContent(
    state: ITransactionDetailsUIState,
    onClose: () -> Unit,
    onEdit: (String) -> Unit,
    onDelete: () -> Unit,
    onShare: (Transaction) -> Unit,
    onRetry: () -> Unit
) {
    when (state) {
        is ITransactionDetailsUIState.Loading -> Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        is ITransactionDetailsUIState.Success -> TransactionDetailsContent(
            transaction = state.transaction,
            category = state.category,
            goal = state.goal,
            onClose = onClose,
            onEdit = { onEdit(state.transaction.id) },
            onDelete = onDelete,
            onShare = { onShare(state.transaction) }
        )
        is ITransactionDetailsUIState.Error -> ErrorScreen(
            messageResId = state.messageResId,
            onRetry = onRetry
        )
    }
}

@Composable
fun TransactionDetailsContent(
    transaction: Transaction,
    category: Category?,
    goal: Goal?,
    onClose: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.add_expense_backgound),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.FillWidth
            )

            Column(modifier = Modifier.fillMaxSize()) {
                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        containerColor = Color.White,
                        title = { Text(stringResource(R.string.delete_confirm_title)) },
                        text = { Text(stringResource(R.string.transaction_delete_confirm_message)) },
                        confirmButton = {
                            DialogConfirmButton(text = stringResource(R.string.action_ok), onClick = {
                                showDeleteDialog = false
                                onDelete()
                            })
                        },
                        dismissButton = {
                            DialogDismissButton(text = stringResource(R.string.action_cancel), onClick = { showDeleteDialog = false })
                        }
                    )
                }
                TransactionDetailsHeader(
                    transaction = transaction,
                    category = category,
                    onClose = onClose,
                    onDelete = { showDeleteDialog = true }
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.transaction_detail_content_spacer)))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_standard))
                ) {
                    TransactionInfoCard(
                        rows = listOf(
                            stringResource(R.string.transaction_detail_date) to transaction.date.format(
                                DateTimeFormatter.ofPattern(stringResource(R.string.date_format_pattern))
                            ),
                            stringResource(R.string.transaction_detail_type) to stringResource(
                                when (transaction.type) {
                                    TransactionType.EXPENSE -> R.string.transaction_type_expense
                                    TransactionType.INCOME -> R.string.transaction_type_income
                                    TransactionType.TRANSFER -> R.string.transaction_type_transfer
                                }
                            )
                        )
                    )

                    TransactionInfoCard(
                        rows = buildList {
                            add(stringResource(R.string.transaction_detail_narration) to transaction.title)
                            add(stringResource(R.string.transaction_detail_description) to transaction.description)
                            if (transaction.type == TransactionType.TRANSFER && goal != null) {
                                add(stringResource(R.string.transaction_detail_goal) to goal.title)
                            } else if (category != null) {
                                add(stringResource(R.string.transaction_detail_category) to category.name)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard)))

                PrimaryButton(
                    text = stringResource(R.string.transaction_detail_edit),
                    onClick = onEdit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TransactionDetailsPreview() {
    PersonalExpenseManagerTheme {
        TransactionDetailsContent(
            transaction = Transaction(
                id = "1",
                title = "Grocery shopping",
                amount = BigDecimal("87.50"),
                date = LocalDate.of(2026, 7, 10),
                currency = Currency.EUR,
                type = TransactionType.EXPENSE,
                categoryId = "cat1",
                description = "Weekly groceries from Lidl",
                paymentMethod = PaymentMethod.CARD
            ),
            category = Category(
                id = "cat1",
                name = "Food",
                iconName = "🛒"
            ),
            goal = null,
            onClose = {},
            onEdit = {},
            onDelete = {},
            onShare = {}
        )
    }
}