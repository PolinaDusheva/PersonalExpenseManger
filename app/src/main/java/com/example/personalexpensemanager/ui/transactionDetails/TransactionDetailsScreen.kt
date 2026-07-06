package com.example.personalexpensemanager.ui.transactionDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.symbol
import com.example.personalexpensemanager.ui.components.appButtons.PrimaryButton
import com.example.personalexpensemanager.ui.transactionDetails.components.TransactionDetailsHeader
import com.example.personalexpensemanager.ui.transactionDetails.components.TransactionInfoCard
import java.time.format.DateTimeFormatter

@Composable
fun TransactionDetailsScreen(
    viewModel: TransactionDetailsViewModel,
    onClose: () -> Unit,
    onShare: (Transaction) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (val s = state) {
        is ITransactionDetailsUIState.Loading -> Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        is ITransactionDetailsUIState.Error -> Box(Modifier.fillMaxSize()) {
            Text(stringResource(s.messageResId), modifier = Modifier.align(Alignment.Center))
        }
        is ITransactionDetailsUIState.Success -> TransactionDetailsContent(
            transaction = s.transaction,
            category = s.category,
            onClose = onClose,
            onShare = { onShare(s.transaction) }
        )
    }
}

@Composable
fun TransactionDetailsContent(
    transaction: Transaction,
    category: Category?,
    onClose: () -> Unit,
    onShare: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize()) {
            TransactionDetailsHeader(
                transaction = transaction,
                category = category,
                onClose = onClose,
                onShare = onShare
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard)))

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
                        stringResource(R.string.transaction_detail_tran_id) to transaction.id,
                        stringResource(R.string.transaction_detail_status) to stringResource(R.string.transaction_detail_status_completed)
                    )
                )

                TransactionInfoCard(
                    rows = listOf(
                        stringResource(R.string.transaction_detail_fee) to "0${transaction.currency.symbol}",
                        stringResource(R.string.transaction_detail_narration) to transaction.title,
                        stringResource(R.string.transaction_detail_description) to transaction.description
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = stringResource(R.string.transaction_detail_share),
                onClick = onShare,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_horizontal))
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard)))
        }
    }
}