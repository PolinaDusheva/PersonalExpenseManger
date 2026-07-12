package com.example.personalexpensemanager.domain

import com.example.personalexpensemanager.domain.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

object TransactionCalculator {

    fun filterCurrentMonth(transactions: List<Transaction>): List<Transaction> {
        val now = LocalDate.now()
        val result = mutableListOf<Transaction>()
        for (transaction in transactions) {
            if (transaction.date.year == now.year && transaction.date.month == now.month) {
                result.add(transaction)
            }
        }
        return result
    }

    fun calculateTotalExpenses(transactions: List<Transaction>): BigDecimal {
        var amount = BigDecimal.ZERO
        for (transaction in transactions) {
            if (transaction.type == TransactionType.EXPENSE) {
                amount += transaction.amount
            }
        }
        return amount
    }

    fun calculateBiggestExpense(transactions: List<Transaction>): BigDecimal {
        var maxExpense = BigDecimal.ZERO
        for (transaction in transactions) {
            if (transaction.type == TransactionType.EXPENSE && transaction.amount > maxExpense) {
                maxExpense = transaction.amount
            }
        }
        return maxExpense
    }
}