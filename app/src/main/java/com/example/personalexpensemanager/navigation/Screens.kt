package com.example.personalexpensemanager.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Transactions : Screen("transactions")
    data object Categories : Screen("categories") {
        const val ARG_RETURN = "returnAfterAdd"
        val routeWithArgs = "categories?$ARG_RETURN={$ARG_RETURN}"
        fun createRoute(returnAfterAdd: Boolean) = "categories?$ARG_RETURN=$returnAfterAdd"
    }
    data object AddExpense : Screen("addExpense")
    data object TransactionDetail : Screen("transactionDetail/{transactionId}") {
        fun createRoute(transactionId: String) = "transactionDetail/$transactionId"

    }
    data object EditTransaction : Screen("editTransaction/{transactionId}") {
        fun createRoute(transactionId: String) = "editTransaction/$transactionId"
    }
    data object Goals : Screen("goals") {
        const val ARG_RETURN = "returnAfterAdd"
        val routeWithArgs = "goals?$ARG_RETURN={$ARG_RETURN}"
        fun createRoute(returnAfterAdd: Boolean) = "goals?$ARG_RETURN=$returnAfterAdd"
    }

    data object Statistics : Screen("statistics")
}