package com.example.personalexpensemanager.ui.goals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.GradientGraphics
import com.example.personalexpensemanager.ui.theme.GradientStart
import java.math.BigDecimal
import java.text.DecimalFormat

@Composable
fun SetBudgetCard(
    currentBudget: BigDecimal?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasBudget = currentBudget != null && currentBudget > BigDecimal.ZERO

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = dimensionResource(R.dimen.elevation),
                shape = RoundedCornerShape(dimensionResource(R.dimen.statistics_card_corner_radius)),
                ambientColor = Color.Black.copy(alpha = 0.15f)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(dimensionResource(R.dimen.statistics_card_corner_radius)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GradientGraphics.primaryHorizontal)
                .padding(dimensionResource(R.dimen.padding_standard)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.category_card_icon_size))
                    .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                    .padding(dimensionResource(R.dimen.padding_small))
            )
            Spacer(Modifier.width(dimensionResource(R.dimen.category_row_icon_spacing)))
            Column {
                Text(
                    text = stringResource(R.string.goals_budget_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = if (hasBudget) "€${DecimalFormat("#,##0.00").format(currentBudget)}"
                    else stringResource(R.string.goals_set_budget),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
    }
}