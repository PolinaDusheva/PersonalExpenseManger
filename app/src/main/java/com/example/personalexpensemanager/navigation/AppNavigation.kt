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
import com.example.personalexpensemanager.ui.addExpense.AddExpenseScreen
import com.example.personalexpensemanager.ui.addExpense.AddExpenseViewModel
import com.example.personalexpensemanager.ui.dashboard.DashboardViewModel
import com.example.personalexpensemanager.ui.transaction.TransactionViewModel

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Transactions : Screen("transactions/{transactionId}")
    data object Categories : Screen("categories/{categoryId}")
    data object TransactionDetail : Screen("transactionDetail/{transactionId}") {
        fun createRoute(transactionId: String) = "transactionDetail/$transactionId"
    }
    data object AddExpense : Screen("addExpense")
}
@Composable
fun AppNavigation(){
    val myNavigationManager = rememberNavController()
    val factory = AppViewModelFactory()

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
            TransactionsScreen(
                viewModel = viewModel,
                onTransactionClick = { id ->
                    myNavigationManager.navigate(Screen.TransactionDetail.createRoute(id))
                }
            )
        }
        composable(Screen.AddExpense.route) {
            val viewModel: AddExpenseViewModel = viewModel(factory = factory)
            AddExpenseScreen(viewModel = viewModel, onBack = {
                myNavigationManager.navigate(Screen.Transactions.route)
            })
        }
//        composable(
//            route = Screen.Categories.route,
//            arguments = listOf(navArgument(name ="categoryId")
//            {
//                type = NavType.StringType
//            })
//        ){backStackEntry ->
//            val categoryId = backStackEntry.arguments?.getString("categoryId")
//            CategoriesScreen(categoryId = categoryId)
//        }
    }

}

