package com.example.personalexpensemanager.ui.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.math.BigDecimal
import java.text.DecimalFormat

@Composable
fun TotalSpentCard(
    label: String,
    amount: BigDecimal,
    highlighted: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    val background = if (highlighted) {
        Modifier.background(
            brush = Brush.linearGradient(listOf(GradientStart, GradientEnd)),
            shape = shape
        )
    } else {
        Modifier.background(Color.White, shape)
    }
    val labelColor = if (highlighted) Color.White.copy(alpha = 0.8f) else Color(0xFF6B7280)
    val amountColor = if (highlighted) Color.White else Color(0xFF1F2937)

    Column(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = shape)
            .then(background)
            .padding(16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = labelColor,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = formatAmount(amount),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}
private fun formatAmount(amount: BigDecimal): String {
    val formatter = DecimalFormat("#,##0.00")
    return "€ ${formatter.format(amount)}"
}

@Preview(showBackground = true)
@Composable
fun TotalSpentCardPreview() {
    PersonalExpenseManagerTheme {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            TotalSpentCard(
                label = "THIS MONTH",
                amount = BigDecimal("1240.50"),
                highlighted = true,
                modifier = Modifier.weight(1f)
            )
            TotalSpentCard(
                label = "SELECTED PERIOD",
                amount = BigDecimal("312.80"),
                modifier = Modifier.weight(1f)
            )
        }
    }
}