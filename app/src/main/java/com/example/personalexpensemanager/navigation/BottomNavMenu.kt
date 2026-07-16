package com.example.personalexpensemanager.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme


data class BottomNavMenu(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

@Composable
fun bottomTabs() = listOf(
    BottomNavMenu(Screen.Dashboard, stringResource(R.string.nav_home), Icons.Filled.Home),
    BottomNavMenu(Screen.Transactions, stringResource(R.string.nav_payments), Icons.Filled.Payments),
    BottomNavMenu(Screen.AddExpense, stringResource(R.string.nav_add), Icons.Filled.Add),
    BottomNavMenu(Screen.Goals, stringResource(R.string.nav_goals), Icons.Filled.Radar),
    BottomNavMenu(Screen.Categories, stringResource(R.string.nav_categories), Icons.Filled.Category),
    //BottomNavMenu(Screen.Statistics, stringResource(R.string.nav_statistics), Icons.Filled.StackedBarChart),
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onTabClick: (Screen) -> Unit
) {
    NavigationBar {
        bottomTabs().forEach { tab ->
            NavigationBarItem(
                selected = currentRoute?.substringBefore("?") == tab.screen.route,
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