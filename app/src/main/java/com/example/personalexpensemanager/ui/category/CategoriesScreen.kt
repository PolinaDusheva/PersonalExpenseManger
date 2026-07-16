package com.example.personalexpensemanager.ui.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.ui.components.AppSnackbarHost
import com.example.personalexpensemanager.ui.components.CategoryCard
import com.example.personalexpensemanager.ui.components.CategoryFormDialog
import com.example.personalexpensemanager.ui.components.DeleteConfirmDialog
import com.example.personalexpensemanager.ui.components.ErrorScreen
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.components.appButtons.GradientIconButton
import com.example.personalexpensemanager.ui.theme.GradientEnd
import com.example.personalexpensemanager.ui.theme.GradientStart
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

private val cardGradients = listOf(GradientStart, GradientEnd)

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel,
    onCategoryAdded: (() -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val formErrors by viewModel.formErrors.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { resId ->
            snackbarHostState.showSnackbar(context.getString(resId))
        }
    }

    CategoriesContent(
        state = state,
        formErrors = formErrors,
        snackbarHostState = snackbarHostState,
        onAdd = viewModel::addCategory,
        onUpdate = viewModel::updateCategory,
        onDelete = viewModel::deleteCategory,
        onRetry = viewModel::retry,
        onNameChanged = viewModel::onNameChanged,
        onNameFieldTouched = viewModel::onNameFieldTouched,
        onIconSelected = viewModel::onIconSelected,
        onIconTouched = viewModel::onIconTouched,
        onStartEditing = viewModel::startEditing,
        onCategoryAdded = onCategoryAdded
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesContent(
    state: ICategoriesUIState,
    formErrors: CategoryFormErrors,
    snackbarHostState: SnackbarHostState,
    onAdd: (name: String, iconName: String) -> Unit,
    onUpdate: (Category) -> Unit,
    onDelete: (categoryId: String) -> Unit,
    onRetry: () -> Unit = {},
    onNameChanged: (String) -> Unit,
    onNameFieldTouched: () -> Unit,
    onIconSelected: (String) -> Unit,
    onIconTouched: () -> Unit,
    onStartEditing: (Category?) -> Unit,
    onCategoryAdded: (() -> Unit)? = null
){
    var showAddDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.categories_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = dimensionResource(R.dimen.padding_small))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Headline(text = stringResource(R.string.categories_title))
                    }
                    GradientIconButton(
                        icon = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.categories_add_description),
                        onClick = {
                            onStartEditing(null)
                            showAddDialog = true
                        }
                    )
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    when (state) {
                        is ICategoriesUIState.Loading ->
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                        is ICategoriesUIState.Error ->
                            ErrorScreen(
                                messageResId = state.messageResId,
                                onRetry = onRetry,
                                modifier = Modifier.align(Alignment.Center)
                            )

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
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.category_grid_spacing)),
                                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.category_grid_spacing))
                            ) {
                                items(
                                    items = state.categories,
                                    key = { it.id }
                                ) { category ->
                                    val index = state.categories.indexOf(category)
                                    CategoryCard(
                                        category = category,
                                        gradient = cardGradients,
                                        onClick = {
                                            onStartEditing(category)
                                            categoryToEdit = category
                                        },
                                        onDelete = { categoryToDelete = category }
                                    )
                                }
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
            nameErrorResId = formErrors.nameErrorResId,
            nameTouched = formErrors.nameTouched,
            iconErrorResId = formErrors.iconErrorResId,
            onNameChanged = onNameChanged,
            onNameFieldTouched = onNameFieldTouched,
            onIconSelected = onIconSelected,
            onIconTouched = onIconTouched,
            onConfirm = { name, icon ->
                onAdd(name, icon)
                showAddDialog = false
                onCategoryAdded?.invoke()
            },
            onDismiss = { showAddDialog = false }
        )
    }

    categoryToEdit?.let { category ->
        CategoryFormDialog(
            title = stringResource(R.string.categories_edit_title),
            initial = category,
            nameErrorResId = formErrors.nameErrorResId,
            nameTouched = formErrors.nameTouched,
            iconErrorResId = formErrors.iconErrorResId,
            onNameChanged = onNameChanged,
            onNameFieldTouched = onNameFieldTouched,
            onIconSelected = onIconSelected,
            onIconTouched = onIconTouched,
            onConfirm = { name, icon -> onUpdate(category.copy(name = name, iconName = icon)); categoryToEdit = null },
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
            formErrors = CategoryFormErrors(),
            snackbarHostState = remember { SnackbarHostState() },
            onAdd = { _, _ -> },
            onUpdate = {},
            onDelete = {},
            onRetry = {},
            onNameChanged = {},
            onNameFieldTouched = {},
            onIconSelected = {},
            onIconTouched = {},
            onStartEditing = {},
        )

    }

}
