package com.example.personalexpensemanager.ui.transactionDetails.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.ui.components.categoryIconFilled

@Composable
fun CategoryChip(category: Category) {
    Surface(shape = RoundedCornerShape(50), color = Color.White) {
        Row(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.transaction_detail_chip_padding_horizontal),
                vertical = dimensionResource(R.dimen.transaction_detail_chip_padding_vertical)
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                categoryIconFilled(category.iconName),
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(R.dimen.transaction_detail_chip_icon_size))
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.transaction_detail_chip_icon_spacing)))
            Text(category.name, fontWeight = FontWeight.Medium)
        }
    }
}