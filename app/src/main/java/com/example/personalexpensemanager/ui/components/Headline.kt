package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

@Composable
fun Headline(text: String, spacerHeight: Dp = 8.dp) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall
    )
    Spacer(modifier = Modifier.height(spacerHeight))
}

@Preview(showBackground = true)
@Composable
fun HeadlinePreview() {
    PersonalExpenseManagerTheme {
        Headline(text = "Последни транзакции")
    }
}
