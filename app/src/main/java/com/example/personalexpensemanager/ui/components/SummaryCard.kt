package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.colorResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.enums.Currency
import java.math.BigDecimal

@Composable
fun SummaryCard(
    title: String,
    amount: BigDecimal,
    currency: Currency,
    modifier: Modifier = Modifier,

) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.summary_card_rounded_corners)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.summary_card_elevation)
        )
    ) {
        Column(
            modifier = Modifier.padding(
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
                color = colorResource(R.color.black)
            )
            Text(
                text = "$amount$currency",
                style = MaterialTheme.typography.titleLarge,
                color = colorResource(R.color.black)
            )
        }
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
