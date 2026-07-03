package com.example.personalexpensemanager.ui.components.appButtons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personalexpensemanager.ui.theme.GradientGraphics
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
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(),
        modifier = modifier
            .fillMaxWidth()
            //.shadow(elevation = 4.dp, shape = RoundedCornerShape(50.dp)),

    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = GradientGraphics.primaryHorizontal,
                    shape = RoundedCornerShape(70.dp)
                )
                .clip(RoundedCornerShape(50.dp))
                .padding(horizontal = 70.dp, vertical = 8.dp)
        ) {
            Text(text)
        }
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
