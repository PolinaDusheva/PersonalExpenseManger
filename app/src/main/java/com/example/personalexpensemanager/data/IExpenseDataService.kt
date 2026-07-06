package com.example.personalexpensemanager.data

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.domain.Transaction
import kotlinx.coroutines.flow.StateFlow

interface IExpenseDataService {

        suspend fun getTransactions(): List<Transaction>
        suspend fun getCategories(): List<Category>
        suspend fun getTransaction(id: String): Transaction?

        val transactions: StateFlow<List<Transaction>>
        val categories: StateFlow<List<Category>>
        val goals: StateFlow<List<Goal>>

        suspend fun addTransaction(transaction: Transaction)
        suspend fun addCategory(category: Category)
        suspend fun updateCategory(category: Category)
        suspend fun deleteCategory(categoryId: String)

        suspend fun addGoal(goal: Goal)
        suspend fun updateGoal(goal: Goal)
        suspend fun deleteGoal(goalId: String)
}