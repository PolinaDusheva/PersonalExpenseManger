package com.example.personalexpensemanager.ui.addExpense.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.GradientGraphics

@Composable
fun <T> RadioButton(
    option: T,
    selected: Boolean,
    label: String,
    onSelect: (T) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = { onSelect(option) })
            .padding(vertical = dimensionResource(R.dimen.add_expense_radio_row_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dimensionResource(R.dimen.add_expense_radio_circle_size))
                .shadow(elevation = dimensionResource(R.dimen.elevation), shape = CircleShape)
                .background(Color.White, CircleShape)
                .border(
                    width = dimensionResource(R.dimen.add_expense_radio_circle_border_width),
                    brush = if (selected) GradientGraphics.primaryHorizontal
                    else Brush.linearGradient(listOf(Color.LightGray, Color.LightGray)),
                    shape = CircleShape
                )
                .padding(dimensionResource(R.dimen.add_expense_radio_circle_padding))
                .clip(CircleShape)
                .then(if (selected) Modifier.background(GradientGraphics.primaryHorizontal) else Modifier)
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.add_expense_radio_label_spacing)))
        Text(text = label)
    }
}