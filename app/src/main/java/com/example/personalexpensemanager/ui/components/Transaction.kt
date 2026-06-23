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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensemanager.domain.Transaction
import java.time.format.DateTimeFormatter

@Composable
fun TransactionItem(transaction: Transaction) {
    val isIncome = transaction.sign == '+'

    // цветове според приход/разход
    val amountColor = if (isIncome) Color(0xFF2E9E5B) else Color(0xFFE53935)
    val circleColor = if (isIncome) Color(0xFFEDE3FB) else Color(0xFFFCE4E4)
    val arrowColor = if (isIncome) Color(0xFF7E3FF2) else Color(0xFFE53935)
    val arrowIcon = if (isIncome) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // кръгчето с иконата
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(circleColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = arrowIcon,
                    contentDescription = null,
                    tint = arrowColor
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = transaction.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = transaction.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = "${transaction.sign}${transaction.amount}${transaction.currency}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = amountColor
        )
    }
}