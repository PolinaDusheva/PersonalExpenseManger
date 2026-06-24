package com.example.personalexpensemanager.ui.category

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.ui.components.CategoryCard
import com.example.personalexpensemanager.ui.components.CategoryFormDialog
import com.example.personalexpensemanager.ui.components.DeleteConfirmDialog


private val cardColors = listOf(
    Color(0xFF7E3FF2),
    Color(0xFF2E9E5B),
    Color(0xFFE5793A),
    Color(0xFF3A7BE5),
    Color(0xFFE53935)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(viewModel: CategoriesViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // локален UI стейт за диалозите
    var showAddDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Категории") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Добави категория")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val s = state) {
                is CategoriesUIState.Loading ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                is CategoriesUIState.Error ->
                    Text(s.message, modifier = Modifier.align(Alignment.Center))

                is CategoriesUIState.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            vertical = dimensionResource(R.dimen.padding_small)
                        ),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = s.categories,
                            key = { it.id }
                        ) { category ->
                            val index = s.categories.indexOf(category)
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

    if (showAddDialog) {
        CategoryFormDialog(
            title = "Нова категория",
            initial = null,
            onConfirm = { name, icon ->
                viewModel.addCategory(name, icon)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }

    categoryToEdit?.let { category ->
        CategoryFormDialog(
            title = "Редакция на категория",
            initial = category,
            onConfirm = { name, icon ->
                viewModel.updateCategory(category.copy(name = name, iconName = icon))
                categoryToEdit = null
            },
            onDismiss = { categoryToEdit = null }
        )
    }

    categoryToDelete?.let { category ->
        DeleteConfirmDialog(
            categoryName = category.name,
            onConfirm = {
                viewModel.deleteCategory(category.id)
                categoryToDelete = null
            },
            onDismiss = { categoryToDelete = null }
        )
    }
}