package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensemanager.domain.Transaction
import java.time.format.DateTimeFormatter
import com.example.personalexpensemanager.R


@Composable
fun TransactionItem(transaction: Transaction){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.padding_small).value.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = transaction.title,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(R.dimen.font_medium).value.sp
            )
            Text(
                text = transaction.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                fontSize = dimensionResource(R.dimen.font_small).value.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "${transaction.sign}${transaction.amount}${transaction.currency}",
            fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(R.dimen.font_medium).value.sp
        )
    }
}