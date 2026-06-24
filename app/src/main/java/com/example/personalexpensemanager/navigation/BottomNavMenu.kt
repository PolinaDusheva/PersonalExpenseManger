package com.example.personalexpensemanager.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme


data class BottomNavMenu(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomTabs = listOf(
    BottomNavMenu(Screen.Dashboard, "Начало", Icons.Filled.Home),
    BottomNavMenu(Screen.Transactions, "Транзакции", Icons.AutoMirrored.Filled.List),
    BottomNavMenu(Screen.AddExpense, "Добави", Icons.Filled.Add),
    BottomNavMenu(Screen.Categories, "Категории", Icons.Filled.Category)
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onTabClick: (Screen) -> Unit
) {
    NavigationBar {
        bottomTabs.forEach { tab ->
            NavigationBarItem(
                selected = currentRoute == tab.screen.route,
                onClick = { onTabClick(tab.screen) },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    PersonalExpenseManagerTheme {
        BottomNavBar(
            currentRoute = Screen.Dashboard.route,
            onTabClick = {}
        )
    }
}