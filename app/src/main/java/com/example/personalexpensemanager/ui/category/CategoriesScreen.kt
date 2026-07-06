package com.example.personalexpensemanager.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.ui.components.CategoryCard
import com.example.personalexpensemanager.ui.components.CategoryFormDialog
import com.example.personalexpensemanager.ui.components.DeleteConfirmDialog
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

private val cardColors = listOf(
    Color(0xFF7E3FF2),
    Color(0xFF2E9E5B),
    Color(0xFFE5793A),
    Color(0xFF3A7BE5),
    Color(0xFFE53935)
)

@Composable
fun CategoriesScreen(viewModel: CategoriesViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CategoriesContent(
        state = state,
        onAdd = viewModel::addCategory,
        onUpdate = viewModel::updateCategory,
        onDelete = viewModel::deleteCategory
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesContent(
    state: ICategoriesUIState,
    onAdd: (name: String, iconName: String) -> Unit,
    onUpdate: (Category) -> Unit,
    onDelete: (categoryId: String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        containerColor = Color.White,
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.categories_add_description))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = dimensionResource(R.dimen.padding_small))
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal))) {
                Headline(text = stringResource(R.string.categories_title))
            }
            Box(modifier = Modifier.fillMaxSize()) {
                when (state) {
                    is ICategoriesUIState.Loading ->
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                    is ICategoriesUIState.Error ->
                        Text(state.message, modifier = Modifier.align(Alignment.Center))

                    is ICategoriesUIState.Success -> {
                        if (state.categories.isEmpty()) {
                            Text(
                                text = stringResource(R.string.categories_empty_state),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                            contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.padding_small)),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.categories,
                                key = { it.id }
                            ) { category ->
                                val index = state.categories.indexOf(category)
                                CategoryCard(
                                    category = category,
                                    color = cardColors[index % cardColors.size],
                                    onClick = { categoryToEdit = category },
                                    onDelete = { categoryToDelete = category }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        CategoryFormDialog(
            title = stringResource(R.string.categories_add_title),
            initial = null,
            onConfirm = { name, icon ->
                onAdd(name, icon)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }

    categoryToEdit?.let { category ->
        CategoryFormDialog(
            title = stringResource(R.string.categories_edit_title),
            initial = category,
            onConfirm = { name, icon ->
                onUpdate(category.copy(name = name, iconName = icon))
                categoryToEdit = null
            },
            onDismiss = { categoryToEdit = null }
        )
    }

    categoryToDelete?.let { category ->
        DeleteConfirmDialog(
            categoryName = category.name,
            onConfirm = {
                onDelete(category.id)
                categoryToDelete = null
            },
            onDismiss = { categoryToDelete = null }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesContentPreview() {
    PersonalExpenseManagerTheme {
        CategoriesContent(
            state = ICategoriesUIState.Success(
                categories = listOf(
                    Category(id = "1", iconName = "food", name = "Храна"),
                    Category(id = "2", iconName = "transport", name = "Транспорт"),
                    Category(id = "3", iconName = "payments", name = "Сметки"),
                    Category(id = "4", iconName = "entertainment", name = "Развлечения")
                )
            ),
            onAdd = { _, _ -> },
            onUpdate = {},
            onDelete = {}
        )
    }
}