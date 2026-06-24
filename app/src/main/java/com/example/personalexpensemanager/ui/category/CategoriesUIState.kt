package com.example.personalexpensemanager.ui.category

import com.example.personalexpensemanager.domain.Category

sealed interface CategoriesUIState {
    data object Loading : CategoriesUIState
    data class Success(val categories: List<Category>) : CategoriesUIState
    data class Error(val message: String) : CategoriesUIState
}