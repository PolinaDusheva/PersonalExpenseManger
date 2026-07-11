// ui/category/CategoryFormErrors.kt
package com.example.personalexpensemanager.ui.category

data class CategoryFormErrors(
    val nameErrorResId: Int? = null,
    val iconErrorResId: Int? = null,
    val nameTouched: Boolean = false,
    val iconTouched: Boolean = false
)