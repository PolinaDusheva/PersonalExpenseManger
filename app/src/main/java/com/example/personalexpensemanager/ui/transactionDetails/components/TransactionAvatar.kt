package com.example.personalexpensemanager.ui.transactionDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.arrowColorRes
import com.example.personalexpensemanager.domain.enums.circleColorRes
import com.example.personalexpensemanager.domain.enums.icon

@Composable
fun TransactionAvatar(transaction: Transaction) {
    Box(
        modifier = Modifier.size(dimensionResource(R.dimen.transaction_detail_avatar_size)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(transaction.type.circleColorRes), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = transaction.type.icon,
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(R.dimen.transaction_detail_avatar_icon_size)),
                tint = colorResource(transaction.type.arrowColorRes)
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(dimensionResource(R.dimen.transaction_detail_badge_size))
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (transaction.paymentMethod == PaymentMethod.CARD) Icons.Filled.CreditCard else Icons.Filled.Payments,
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(R.dimen.transaction_detail_badge_icon_size))
            )
        }
    }
}