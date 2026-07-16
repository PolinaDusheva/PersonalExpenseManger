package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview

import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.math.BigDecimal

@Composable
fun SummaryCard(
    title: String,
    amount: BigDecimal,
    currency: Currency,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false
) {
    val shape = RoundedCornerShape(dimensionResource(R.dimen.summary_card_rounded_corners))
    val background = if (highlighted) {
        Modifier.background(
            brush = Brush.linearGradient(listOf(GradientStart, GradientEnd)),
            shape = shape
        )
    } else {
        Modifier.background(Color.White, shape)
    }
    val titleColor = if (highlighted) Color.White.copy(alpha = 0.8f) else colorResource(R.color.black)
    val amountColor = if (highlighted) Color.White else colorResource(R.color.black)

    Column(
        modifier = modifier
            .shadow(
                elevation = dimensionResource(R.dimen.elevation),
                shape = shape,
            )
            .then(background)
            .padding(
                start = dimensionResource(R.dimen.summary_card_padding_start),
                top = dimensionResource(R.dimen.summary_card_padding_top),
                end = dimensionResource(R.dimen.summary_card_padding_end),
                bottom = dimensionResource(R.dimen.summary_card_padding_bottom),
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.summary_card_content_spacing)),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = titleColor
        )
        Text(
            text = "$amount$currency",
            style = MaterialTheme.typography.titleLarge,
            color = amountColor
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SummaryCardPreview() {
    PersonalExpenseManagerTheme {
        SummaryCard(
            title = "Общо за месеца",
            amount = BigDecimal(1250.50),
            currency = Currency.EUR
        )
    }
}
