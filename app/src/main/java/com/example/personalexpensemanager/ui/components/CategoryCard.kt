package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

@Composable
fun CategoryCard(
    category: Category,
    color: Color,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.6f)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.category_card_corner_radius)))
            .background(color)
            .clickable { onClick() }
            .padding(dimensionResource(R.dimen.category_card_padding))
    ) {

        Icon(
            imageVector = categoryIconFilled(category.iconName),
            contentDescription = null,
            tint = colorResource(R.color.white),
            modifier = Modifier
                .size(dimensionResource(R.dimen.category_card_icon_size))
                .align(Alignment.TopEnd)
        )

        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.category_delete),
            tint = colorResource(R.color.white),
            modifier = Modifier
                .size(dimensionResource(R.dimen.category_card_delete_icon_size))
                .align(Alignment.BottomEnd)
                .clickable { onDelete() }
        )
        Text(
            text = category.name,
            color = colorResource(R.color.white),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CategoryCardPreview() {
    PersonalExpenseManagerTheme {
        CategoryCard(
            category = Category(id = "1", iconName = "food", name = "Храна"),
            color = Color(0xFF7E3FF2),
            onClick = {},
            onDelete = {}
        )
    }
}