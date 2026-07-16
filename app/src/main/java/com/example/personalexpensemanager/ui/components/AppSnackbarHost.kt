package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.GradientGraphics
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

@Composable
fun AppSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState) { data ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_standard))
                .background(
                    brush = GradientGraphics.primaryHorizontal,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.statistics_card_corner_radius))
                )
                .padding(dimensionResource(R.dimen.padding_standard)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = data.visuals.message,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppSnackbarHostPreview() {
    PersonalExpenseManagerTheme {
        val hostState = remember { SnackbarHostState() }
        androidx.compose.runtime.LaunchedEffect(Unit) {
            hostState.showSnackbar("Failed to delete transaction")
        }
        AppSnackbarHost(hostState)
    }
}