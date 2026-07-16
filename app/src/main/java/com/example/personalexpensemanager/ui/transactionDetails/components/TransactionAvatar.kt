package com.example.personalexpensemanager.ui.transactionDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.icon
import com.example.personalexpensemanager.ui.components.categoryIconFilled
import com.example.personalexpensemanager.ui.theme.GradientGraphics

@Composable
fun TransactionAvatar(transaction: Transaction, category: Category?) {
    val iconBrush = GradientGraphics.primaryHorizontal
    val displayIcon = category?.let { categoryIconFilled(it.iconName) } ?: transaction.type.icon

    Box(
        modifier = Modifier
            .size(dimensionResource(R.dimen.transaction_detail_avatar_size))
            .background(Color.White, CircleShape)
            .border(
                width = dimensionResource(R.dimen.transaction_icon_border_width),
                brush = iconBrush,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = displayIcon,
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(dimensionResource(R.dimen.transaction_detail_avatar_icon_size))
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(brush = iconBrush, blendMode = BlendMode.SrcAtop)
                    }
                }
        )
    }
}