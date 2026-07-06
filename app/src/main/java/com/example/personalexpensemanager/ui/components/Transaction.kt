package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Transaction
import java.time.format.DateTimeFormatter
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.time.LocalDate
import com.example.personalexpensemanager.domain.enums.TransactionType
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.amountColorRes
import com.example.personalexpensemanager.domain.enums.arrowColorRes
import com.example.personalexpensemanager.domain.enums.circleColorRes
import com.example.personalexpensemanager.domain.enums.icon
import com.example.personalexpensemanager.domain.enums.signSymbol
import com.example.personalexpensemanager.domain.enums.symbol
import java.math.BigDecimal


@Composable
fun TransactionItem(transaction: Transaction) {
    val amountColor = colorResource(transaction.type.amountColorRes)
    val circleColor = colorResource(transaction.type.circleColorRes)
    val arrowColor = colorResource(transaction.type.arrowColorRes)
    val arrowIcon = transaction.type.icon

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.transaction_row_vertical_padding)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.transaction_icon_circle_size))
                    .background(circleColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = arrowIcon,
                    contentDescription = null,
                    tint = arrowColor
                )
            }
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.transaction_icon_text_spacing)))
            Column {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = transaction.date.format(DateTimeFormatter.ofPattern(stringResource(R.string.date_format_pattern))),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = "${transaction.type.signSymbol}${transaction.amount}${transaction.currency.symbol}",
            style = MaterialTheme.typography.titleMedium,
            color = amountColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemExpensePreview() {
    PersonalExpenseManagerTheme {
        TransactionItem(
            transaction = Transaction(
                id = "1",
                title = "Супермаркет",
                amount = BigDecimal("150.0"),
                date = LocalDate.of(2026, 6, 24),
                currency = Currency.EUR,
                type = TransactionType.EXPENSE,
                categoryId = "1",
                description = "Пазаруване",
                paymentMethod = PaymentMethod.CARD
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemIncomePreview() {
    PersonalExpenseManagerTheme {
        TransactionItem(
            transaction = Transaction(
                id = "2",
                title = "Заплата",
                amount = BigDecimal("2500.0"),
                date = LocalDate.of(2026, 6, 24),
                currency = Currency.EUR, type = TransactionType.EXPENSE,
                categoryId = "1",
                description = "Месечна заплата",
                paymentMethod = PaymentMethod.CARD
            )
        )
    }
}