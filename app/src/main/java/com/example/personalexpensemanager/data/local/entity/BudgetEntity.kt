package com.example.personalexpensemanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey val id: Int = ID_MONTHLY_BUDGET,
    val amount: BigDecimal
) {
    companion object {
        const val ID_MONTHLY_BUDGET = 1
        const val ID_DAILY_LIMIT = 2
    }
}