package com.example.personalexpensemanager.ui.transactionDetails.components

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.amountColorRes
import com.example.personalexpensemanager.domain.enums.signSymbol
import com.example.personalexpensemanager.domain.enums.symbol
import com.example.personalexpensemanager.ui.components.appButtons.GradientIconButton

@Composable
fun TransactionDetailsHeader(
    transaction: Transaction,
    category: Category?,
    onClose: () -> Unit,
    onDelete: () -> Unit
) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.transaction_detail_header_height))
        ) {
            Image(
                painter = painterResource(id = R.drawable.waves_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(dimensionResource(R.dimen.padding_horizontal)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GradientIconButton(
                        icon = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.action_close),
                        onClick = onClose
                    )
                    GradientIconButton(
                        icon = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.transaction_detail_delete),
                        onClick = onDelete
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TransactionAvatar(transaction, category)

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

                    Text(
                        text = stringResource(
                            R.string.transaction_detail_paid_via,
                            stringResource(
                                if (transaction.paymentMethod == PaymentMethod.CARD) R.string.payment_method_card
                                else R.string.payment_method_cash
                            )
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "${transaction.type.signSymbol}${transaction.amount}${transaction.currency.symbol}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(transaction.type.amountColorRes)
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

                    if (category != null) {
                        CategoryChip(category)
                    }
                }
            }
        }
    }
