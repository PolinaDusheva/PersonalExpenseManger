package com.example.personalexpensemanager.domain

import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: String,
    val iconName: String,
    val name: String,
    val progress: Float,
    val percentage: String
)