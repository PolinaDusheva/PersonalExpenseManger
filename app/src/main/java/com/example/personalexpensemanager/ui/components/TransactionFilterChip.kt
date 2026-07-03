package com.example.personalexpensemanager.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.personalexpensemanager.ui.theme.GradientGraphics
@Composable
fun TransactionFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    val shape = RoundedCornerShape(50.dp)
    Box(
        modifier = modifier
            .then(
                if (selected) {
                    Modifier
                        .clip(shape)
                        .background(brush = GradientGraphics.primaryHorizontal, shape = shape)
                } else {
                    Modifier
                        .shadow(elevation = 3.dp, shape = shape)
                        .background(Color.White, shape)
                        .border(width = 1.dp, brush = GradientGraphics.primaryHorizontal, shape = shape)
                }
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            if (trailingIcon != null) {
                Spacer(Modifier.width(4.dp))
                trailingIcon()
            }
        }
    }
}

