package com.example.personalexpensemanager.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

val categoryIconOptions = listOf(
    "food", "transport", "payments", "entertainment", "health",
    "shopping", "education", "travel", "home", "groceries",
    "fitness", "pets", "gifts", "subscriptions", "savings",
    "salary", "coffee", "clothing", "technology", "other"
)

fun categoryIconFilled(iconName: String): ImageVector = when (iconName) {
    "food" -> Icons.Filled.Restaurant
    "transport" -> Icons.Filled.DirectionsCar
    "payments" -> Icons.Filled.Payments
    "entertainment" -> Icons.Filled.Movie
    "health" -> Icons.Filled.LocalHospital
    "shopping" -> Icons.Filled.ShoppingCart
    "education" -> Icons.Filled.School
    "travel" -> Icons.Filled.Flight
    "home" -> Icons.Filled.Home
    "groceries" -> Icons.Filled.LocalGroceryStore
    "fitness" -> Icons.Filled.FitnessCenter
    "pets" -> Icons.Filled.Pets
    "gifts" -> Icons.Filled.CardGiftcard
    "subscriptions" -> Icons.Filled.Subscriptions
    "savings" -> Icons.Filled.Savings
    "salary" -> Icons.Filled.AttachMoney
    "coffee" -> Icons.Filled.LocalCafe
    "clothing" -> Icons.Filled.Checkroom
    "technology" -> Icons.Filled.Devices
    else -> Icons.Filled.Category
}

fun categoryIconOutlined(iconName: String): ImageVector = when (iconName) {
    "food" -> Icons.Outlined.Restaurant
    "transport" -> Icons.Outlined.DirectionsCar
    "payments" -> Icons.Outlined.Payments
    "entertainment" -> Icons.Outlined.Movie
    "health" -> Icons.Outlined.LocalHospital
    "shopping" -> Icons.Outlined.ShoppingCart
    "education" -> Icons.Outlined.School
    "travel" -> Icons.Outlined.Flight
    "home" -> Icons.Outlined.Home
    "groceries" -> Icons.Outlined.LocalGroceryStore
    "fitness" -> Icons.Outlined.FitnessCenter
    "pets" -> Icons.Outlined.Pets
    "gifts" -> Icons.Outlined.CardGiftcard
    "subscriptions" -> Icons.Outlined.Subscriptions
    "savings" -> Icons.Outlined.Savings
    "salary" -> Icons.Outlined.AttachMoney
    "coffee" -> Icons.Outlined.LocalCafe
    "clothing" -> Icons.Outlined.Checkroom
    "technology" -> Icons.Outlined.Devices
    else -> Icons.Outlined.Category
}
