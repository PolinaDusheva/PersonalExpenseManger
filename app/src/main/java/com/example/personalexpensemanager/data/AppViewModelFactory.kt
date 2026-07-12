package com.example.personalexpensemanager.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.personalexpensemanager.data.local.AppDatabase
import com.example.personalexpensemanager.ui.addExpense.AddExpenseViewModel
import com.example.personalexpensemanager.ui.dashboard.DashboardViewModel
import com.example.personalexpensemanager.ui.transaction.TransactionViewModel
import com.example.personalexpensemanager.ui.category.CategoriesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import com.example.personalexpensemanager.ui.statistics.StatisticsViewModel
class AppViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {

    private val database = AppDatabase.getInstance(context)
    private val applicationScope = CoroutineScope(SupervisorJob()+ Dispatchers.Main)
    val dataService: IExpenseDataService = RoomExpenseDataService(
        categoryDao = database.categoryDao(),
        transactionDao = database.transactionDao(),
        goalDao = database.goalDao(),
        scope = applicationScope
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) ->
                DashboardViewModel(dataService = dataService) as T

            modelClass.isAssignableFrom(TransactionViewModel::class.java) ->
                TransactionViewModel(dataService = dataService) as T

            modelClass.isAssignableFrom(AddExpenseViewModel::class.java) ->
                AddExpenseViewModel(dataService = dataService) as T

            modelClass.isAssignableFrom(CategoriesViewModel::class.java) ->
                CategoriesViewModel(dataService = dataService) as T

            modelClass.isAssignableFrom(StatisticsViewModel::class.java) ->
                StatisticsViewModel(dataService = dataService) as T

            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}