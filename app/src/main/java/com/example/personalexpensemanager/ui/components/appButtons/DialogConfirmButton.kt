package com.example.personalexpensemanager.ui.components.appButtons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.GradientGraphics
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

@Composable
fun DialogConfirmButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(dimensionResource(R.dimen.primary_button_corner_radius))
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(brush = GradientGraphics.primaryHorizontal, shape = shape)
                .clip(shape)
                .padding(
                    horizontal = dimensionResource(R.dimen.dialog_button_padding_horizontal),
                    vertical = dimensionResource(R.dimen.padding_small)
                )
        ) {
            Text(text = text, color = Color.White)
        }
    }
}

@Composable
fun DialogDismissButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(dimensionResource(R.dimen.primary_button_corner_radius))
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(
                    width = dimensionResource(R.dimen.primary_button_border_width),
                    brush = GradientGraphics.primaryHorizontal,
                    shape = shape
                )
                .background(Color.White, shape)
                .clip(shape)
                .padding(
                    horizontal = dimensionResource(R.dimen.dialog_button_padding_horizontal),
                    vertical = dimensionResource(R.dimen.padding_small)
                )
        ) {
            Text(text = text, color = GradientStart)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DialogButtonsPreview() {
    PersonalExpenseManagerTheme {
        androidx.compose.foundation.layout.Row {
            DialogDismissButton(text = "Cancel", onClick = {})
            DialogConfirmButton(text = "OK", onClick = {})
        }
    }
}