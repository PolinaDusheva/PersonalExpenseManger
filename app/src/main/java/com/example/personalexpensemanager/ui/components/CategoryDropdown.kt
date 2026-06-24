package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategorySelected: (String?) -> Unit,
    includeAll: Boolean = false,
    allLabel: String = "Всички",
    placeholder: String = "Изберете категория"
) {
    var expanded by remember { mutableStateOf(false) }

    val label = categories.find { it.id == selectedCategoryId }?.name
        ?: if (includeAll) allLabel else placeholder

    Box {
        FilterChip(
            selected = selectedCategoryId != null,
            onClick = { expanded = true },
            label = { Text(label) },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (includeAll) {
                DropdownMenuItem(
                    text = { Text(allLabel) },
                    onClick = {
                        onCategorySelected(null)
                        expanded = false
                    }
                )
            }
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        onCategorySelected(category.id)
                        expanded = false
                    }
                )
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun CategoryDropdownPreview() {
//    PersonalExpenseManagerTheme {
//        CategoryDropdown(
//            categories = listOf(
//                Category("1", "restaurant", "Храна", 0.6f, "60%"),
//                Category("2", "car", "Транспорт", 0.3f, "30%")
//            ),
//            selectedCategoryId = null,
//            onCategorySelected = {},
//            includeAll = true
//        )
//    }
//}