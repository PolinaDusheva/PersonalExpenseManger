package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.personalexpensemanager.domain.Category
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

@Composable
fun CategoryItem(
    category: Category,
    categorySize: Float
) {
    val icon = when (category.iconName) {
        "restaurant" -> Icons.Outlined.Restaurant
        "car" -> Icons.Outlined.DirectionsCar
        "payments" -> Icons.Outlined.Payments
        else -> Icons.Outlined.Category
    }
    Column{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.padding_small)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.category_row_icon_spacing)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = category.name)
            Text(text = category.name)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "${(categorySize*100).toInt()}%")
        }
        LinearProgressIndicator(
            progress = categorySize,
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.category_progress_height))
                .padding(bottom = dimensionResource(R.dimen.padding_small))
        )

    }

}

//@Preview(showBackground = true)
//@Composable
//fun CategoryItemPreview() {
//    PersonalExpenseManagerTheme {
//        CategoryItem(
//            category = Category(id = "1", iconName = "restaurant", name = "Храна")
//        )
//    }
//}
