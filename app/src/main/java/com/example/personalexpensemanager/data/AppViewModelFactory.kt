package com.example.personalexpensemanager.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.personalexpensemanager.ui.dashboard.DashboardViewModel
import com.example.personalexpensemanager.ui.transaction.TransactionViewModel

class AppViewModelFactory(
    private val dataService: ExpenseDataService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) ->
                DashboardViewModel(dataService) as T

            modelClass.isAssignableFrom(TransactionViewModel::class.java) ->
                TransactionViewModel(dataService) as T

//            modelClass.isAssignableFrom(CategoriesViewModel::class.java) ->
//                CategoriesViewModel(dataService) as T

            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}