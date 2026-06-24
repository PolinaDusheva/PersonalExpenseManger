package com.example.personalexpensemanager.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.ExpenseDataService
import com.example.personalexpensemanager.domain.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val dataService: ExpenseDataService
): ViewModel() {
    private val _uiState = MutableStateFlow<CategoriesUIState>(CategoriesUIState.Loading)
    val uiState: StateFlow<CategoriesUIState> = _uiState
    init{load()}

    fun load(){
        viewModelScope.launch {
            _uiState.value = CategoriesUIState.Loading
            try {
                _uiState.value = CategoriesUIState.Success(dataService.getCategories())
            } catch (e: Exception) {
                _uiState.value = CategoriesUIState.Error(e.message ?: "Грешка")
            }
        }
    }
    fun addCategory(
        name: String, iconName: String) {
        viewModelScope.launch {
            val category = Category(
                id = java.util.UUID.randomUUID().toString(),
                name = name,
                iconName = iconName
                )
            dataService.addCategory(category)
            load()
        }
    }
    fun updateCategory(category: Category) {
        viewModelScope.launch {
            dataService.updateCategory(category)
            load()
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            dataService.deleteCategory(categoryId)
            load()
        }
    }

}