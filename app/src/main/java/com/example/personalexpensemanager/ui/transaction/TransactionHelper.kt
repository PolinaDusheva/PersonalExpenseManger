package com.example.personalexpensemanager.domain

import com.example.personalexpensemanager.domain.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

object TransactionHelper {

    fun filterCurrentMonth(transactions: List<Transaction>): List<Transaction> {
        val now = LocalDate.now()
        return transactions.filter { it.date.year == now.year && it.date.month == now.month }
    }

    fun calculateTotalExpenses(transactions: List<Transaction>): BigDecimal =
        transactions
            .filter { it.type == TransactionType.EXPENSE }
            .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }

    fun calculateBiggestExpense(transactions: List<Transaction>): BigDecimal =
        transactions
            .filter { it.type == TransactionType.EXPENSE }
            .maxOfOrNull { it.amount } ?: BigDecimal.ZERO
}