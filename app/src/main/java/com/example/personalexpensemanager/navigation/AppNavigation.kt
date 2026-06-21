package com.example.personalexpensemanager.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import com.example.personalexpensemanager.ui.dashboard.DashboardScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.personalexpensemanager.ui.category.CategoriesScreen
import com.example.personalexpensemanager.ui.transaction.TransactionsScreen
import androidx.navigation.navArgument
import com.example.personalexpensemanager.data.AppViewModelFactory
import com.example.personalexpensemanager.data.FakeExpenseDataService
import com.example.personalexpensemanager.ui.dashboard.DashboardViewModel
import com.example.personalexpensemanager.ui.transaction.TransactionViewModel

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Transactions : Screen("transactions/{transactionId}")
    data object Categories : Screen("categories/{categoryId}")
}
@Composable
fun AppNavigation(){
    val myNavigationManager = rememberNavController()
    val factory = AppViewModelFactory(FakeExpenseDataService())

    NavHost(
        navController = myNavigationManager,
        startDestination = Screen.Transactions.route
    ) {
        composable(Screen.Dashboard.route) {
            val viewModel: DashboardViewModel = viewModel(factory = factory)
            DashboardScreen(viewModel = viewModel)
        }
        composable(Screen.Transactions.route){
            val viewModel: TransactionViewModel = viewModel(factory =  factory)
            TransactionsScreen(viewModel = viewModel)
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

