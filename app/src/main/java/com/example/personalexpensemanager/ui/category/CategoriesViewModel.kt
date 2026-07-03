package com.example.personalexpensemanager.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.ExpenseDataService
import com.example.personalexpensemanager.domain.Category
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val dataService: ExpenseDataService
) : ViewModel() {

    val uiState: StateFlow<CategoriesUIState> = dataService.categories
        .map { categories -> CategoriesUIState.Success(categories) as CategoriesUIState }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoriesUIState.Loading
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