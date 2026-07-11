package com.example.personalexpensemanager.ui.addExpense.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.GradientGraphics

@Composable
private fun Modifier.gradientBorderWhenFocused(interactionSource: MutableInteractionSource): Modifier {
    val isFocused by interactionSource.collectIsFocusedAsState()
    return if (isFocused) {
        this.border(
            width = dimensionResource(R.dimen.add_expense_field_border_width),
            brush = GradientGraphics.primaryHorizontal,
            shape = RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius))
        )
    } else {
        this
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val interactionSource = remember { MutableInteractionSource() }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = dimensionResource(R.dimen.add_expense_field_elevation),
                shape = RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius))
            )
            .background(Color.White, RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius)))
            .gradientBorderWhenFocused(interactionSource),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius))
    )
}