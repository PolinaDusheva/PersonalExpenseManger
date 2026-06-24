package com.example.personalexpensemanager.ui.components.appButtons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryButtonPreview() {
    PersonalExpenseManagerTheme {
        PrimaryButton(text = "Запази", onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryButtonDisabledPreview() {
    PersonalExpenseManagerTheme {
        PrimaryButton(text = "Записва се...", onClick = {}, enabled = false)
    }
}
