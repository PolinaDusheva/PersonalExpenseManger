package com.example.personalexpensemanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import com.example.personalexpensemanager.ui.dashboard.DashboardScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.personalexpensemanager.ui.category.CategoriesScreen
import com.example.personalexpensemanager.ui.transaction.TransactionsScreen
import androidx.navigation.navArgument
import com.example.personalexpensemanager.data.MockDataService

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Transactions : Screen("transactions/{transactionId}")
    data object Categories : Screen("categories/{categoryId}")
}
@Composable
fun AppNavigation(){
    val myNavigationManager = rememberNavController()

    NavHost(
        navController = myNavigationManager,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            val dataService = MockDataService()
            DashboardScreen(
                transactions = dataService.getTransaction(),
                categories = dataService.getCategories()
            )
        }
        composable(
            route = Screen.Transactions.route,
            arguments = listOf(navArgument(name ="transactionId")
            {
                type = NavType.StringType
            })
        ){ backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")
            TransactionsScreen(transactionId = transactionId)
        }
        composable(
            route = Screen.Categories.route,
            arguments = listOf(navArgument(name ="categoryId")
            {
                type = NavType.StringType
            })
        ){backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            CategoriesScreen(categoryId = categoryId)
        }
    }

}

