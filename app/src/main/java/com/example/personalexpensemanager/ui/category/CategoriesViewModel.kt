package com.example.personalexpensemanager.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Category
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val dataService: IExpenseDataService
) : ViewModel() {

    val uiState: StateFlow<ICategoriesUIState> = dataService.categories
        .map { categories -> ICategoriesUIState.Success(categories) as ICategoriesUIState }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ICategoriesUIState.Loading
        )

    fun addCategory(name: String, iconName: String) {
        viewModelScope.launch {
            val category = Category(
                id = java.util.UUID.randomUUID().toString(),
                name = name,
                iconName = iconName
            )
            dataService.addCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            dataService.updateCategory(category)
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            dataService.deleteCategory(categoryId)
        }
    }
}