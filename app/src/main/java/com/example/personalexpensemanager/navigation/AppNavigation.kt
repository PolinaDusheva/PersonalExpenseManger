package com.example.personalexpensemanager.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.personalexpensemanager.data.AppViewModelFactory
import com.example.personalexpensemanager.ui.addExpense.AddExpenseScreen
import com.example.personalexpensemanager.ui.addExpense.AddExpenseViewModel
import com.example.personalexpensemanager.ui.category.CategoriesScreen
import com.example.personalexpensemanager.ui.category.CategoriesViewModel
import com.example.personalexpensemanager.ui.dashboard.DashboardScreen
import com.example.personalexpensemanager.ui.dashboard.DashboardViewModel
import com.example.personalexpensemanager.ui.goals.GoalsScreen
import com.example.personalexpensemanager.ui.goals.GoalsViewModel
import com.example.personalexpensemanager.ui.transaction.TransactionViewModel
import com.example.personalexpensemanager.ui.transaction.TransactionsScreen
import com.example.personalexpensemanager.ui.transactionDetails.TransactionDetailsScreen
import com.example.personalexpensemanager.ui.transactionDetails.TransactionDetailsViewModel
import com.example.personalexpensemanager.ui.statistics.StatisticsScreen
import com.example.personalexpensemanager.ui.statistics.StatisticsViewModel
@Composable
fun AppNavigation() {
    val myNavigationManager = rememberNavController()
    val context = LocalContext.current
    val factory = remember { AppViewModelFactory(context) }

    val backStackEntry by myNavigationManager.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onTabClick = { screen ->
                    myNavigationManager.navigate(screen.route) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = myNavigationManager,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                val viewModel: DashboardViewModel = viewModel(factory = factory)
                DashboardScreen(
                    viewModel = viewModel,
                    onTransactionClick = { id ->
                        myNavigationManager.navigate(Screen.TransactionDetail.createRoute(id))
                    }
                )
            }
            composable(Screen.Transactions.route) {
                val viewModel: TransactionViewModel = viewModel(factory = factory)
                TransactionsScreen(
                    viewModel = viewModel,
                    onBack = { myNavigationManager.popBackStack() },
                    onTransactionClick = { id ->
                        myNavigationManager.navigate(Screen.TransactionDetail.createRoute(id))
                    },
                    onStatisticsClick = {
                        myNavigationManager.navigate(Screen.Statistics.route)
                    }
                )
            }
            composable(
                Screen.TransactionDetail.route,
                arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString("transactionId") ?: return@composable
                val viewModel: TransactionDetailsViewModel = viewModel(
                    factory = factory.transactionDetailsFactory(id)
                )
                TransactionDetailsScreen(
                    viewModel = viewModel,
                    onClose = { myNavigationManager.popBackStack() },
                    onEdit = { transactionId ->
                        myNavigationManager.navigate(Screen.EditTransaction.createRoute(transactionId))
                    }
                )
            }
            composable(Screen.AddExpense.route) {
                val viewModel: AddExpenseViewModel = viewModel(factory = factory)
                AddExpenseScreen(
                    viewModel = viewModel,
                    onBack = { myNavigationManager.popBackStack() },
                    onNavigateToCategories = {
                        myNavigationManager.navigate(Screen.Categories.createRoute(true))
                    },
                    onNavigateToGoals = {
                        myNavigationManager.navigate(Screen.Goals.createRoute(true))
                    }
                )
            }
            composable(
                Screen.EditTransaction.route,
                arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
            ) { entry ->
                val transactionId = entry.arguments?.getString("transactionId") ?: return@composable
                val viewModel: AddExpenseViewModel = viewModel(
                    factory = factory.editTransactionFactory(transactionId)
                )
                AddExpenseScreen(
                    viewModel = viewModel,
                    onBack = { myNavigationManager.popBackStack() },
                    onNavigateToCategories = {
                        myNavigationManager.navigate(Screen.Categories.route)
                    }
                )
            }
            composable(
                route = Screen.Categories.routeWithArgs,
                arguments = listOf(navArgument(Screen.Categories.ARG_RETURN) {
                    type = NavType.BoolType
                    defaultValue = false
                })
            ) { entry ->
                val returnAfterAdd = entry.arguments?.getBoolean(Screen.Categories.ARG_RETURN) ?: false
                val viewModel: CategoriesViewModel = viewModel(factory = factory)
                CategoriesScreen(
                    viewModel = viewModel,
                    onCategoryAdded = if (returnAfterAdd) {
                        { myNavigationManager.popBackStack() }
                    } else null
                )
            }
            composable(
                route = Screen.Goals.routeWithArgs,
                arguments = listOf(navArgument(Screen.Goals.ARG_RETURN) {
                    type = NavType.BoolType
                    defaultValue = false
                })
            ) { entry ->
                val returnAfterAdd = entry.arguments?.getBoolean(Screen.Goals.ARG_RETURN) ?: false
                val viewModel: GoalsViewModel = viewModel(factory = factory)
                GoalsScreen(
                    viewModel = viewModel,
                    onGoalAdded = if (returnAfterAdd) {
                        { myNavigationManager.popBackStack() }
                    } else null
                )
            }
            composable(Screen.Statistics.route) {
                val viewModel: StatisticsViewModel = viewModel(factory = factory)
                StatisticsScreen(
                    viewModel = viewModel,
                    onBack = { myNavigationManager.popBackStack() }
                )
            }
        }
    }
}
