package com.example.personalexpensemanager.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Transactions : Screen("transactions")
    data object Categories : Screen("categories")
    data object AddExpense : Screen("addExpense")
    data object TransactionDetail : Screen("transactionDetail/{transactionId}") {
        fun createRoute(transactionId: String) = "transactionDetail/$transactionId"

    }
    data object EditTransaction : Screen("editTransaction/{transactionId}") {
        fun createRoute(transactionId: String) = "editTransaction/$transactionId"
    }
    data object Goals:Screen("goals")
}