package com.example.personalexpensemanager.ui.category

import com.example.personalexpensemanager.domain.Category

sealed interface ICategoriesUIState {
    data object Loading : ICategoriesUIState
    data class Success(val categories: List<Category>) : ICategoriesUIState
    data class Error(val messageResId: Int) : ICategoriesUIState
}