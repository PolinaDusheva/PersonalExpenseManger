package com.example.personalexpensemanager.data

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import kotlinx.coroutines.flow.StateFlow

interface ExpenseDataService {

        suspend fun getTransactions(): List<Transaction>
        suspend fun getCategories(): List<Category>
        suspend fun getTransaction(id: String): Transaction?

        val transactions: StateFlow<List<Transaction>>
        val categories: StateFlow<List<Category>>

        suspend fun addTransaction(transaction: Transaction)
        suspend fun addCategory(category: Category)
        suspend fun updateCategory(category: Category)
        suspend fun deleteCategory(categoryId: String)

}