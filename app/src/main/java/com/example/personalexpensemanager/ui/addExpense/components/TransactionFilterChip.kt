package com.example.personalexpensemanager.ui.addExpense.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.GradientGraphics
@Composable
fun TransactionFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    val shape = RoundedCornerShape(dimensionResource(R.dimen.filter_chip_corner_radius))
    Box(
        modifier = modifier
            .then(
                if (selected) {
                    Modifier
                        .clip(shape)
                        .background(brush = GradientGraphics.primaryHorizontal, shape = shape)
                } else {
                    Modifier
                        .shadow(elevation = dimensionResource(R.dimen.elevation), shape = shape)
                        .background(Color.White, shape)
                        .border(width = dimensionResource(R.dimen.filter_chip_border_width), brush = GradientGraphics.primaryHorizontal, shape = shape)
                }
            )
            .clickable { onClick() }
            .padding(horizontal = dimensionResource(R.dimen.padding_standard), vertical = dimensionResource(R.dimen.padding_small))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            if (trailingIcon != null) {
                Spacer(Modifier.width(dimensionResource(R.dimen.statistics_card_spacing)))
                trailingIcon()
            }
        }
    }
}

