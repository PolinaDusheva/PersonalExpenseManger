package com.example.personalexpensemanager.data

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction

interface ExpenseDataService {
    suspend fun getTransactions(): List<Transaction>
    suspend fun  getCategories(): List<Category>
    fun addTransaction(transaction: Transaction)

}