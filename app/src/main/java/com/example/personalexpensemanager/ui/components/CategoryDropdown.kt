package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.ui.addExpense.components.TransactionFilterChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit,
    includeAll: Boolean = false,
    allLabel: String = stringResource(R.string.all_categories),
    placeholder: String = stringResource(R.string.category_dropdown_placeholder),
    modifier: Modifier = Modifier,
    onChipClicked: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    val label = selectedCategory?.name
        ?: if (includeAll) allLabel else placeholder

    Box(modifier = modifier) {
        TransactionFilterChip(
            text = label,
            selected = selectedCategory != null,
            onClick = {
                if (onChipClicked != null) onChipClicked() else expanded = true
            },
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = if (selectedCategory != null)
                        MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface
                )
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
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}