package com.example.personalexpensemanager.ui.transactionDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.example.personalexpensemanager.R

@Composable
fun TransactionInfoCard(rows: List<Pair<String, String>>) {
    Surface(
        shape = RoundedCornerShape(dimensionResource(R.dimen.transaction_detail_card_corner_radius)),
        color = Color.White,
        shadowElevation = dimensionResource(R.dimen.transaction_detail_info_card_elevation)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.transaction_detail_card_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.transaction_detail_card_row_spacing))
        ) {
            rows.forEach { (label, value) -> DetailRow(label, value) }
        }
    }
}