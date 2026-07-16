package com.example.personalexpensemanager.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.personalexpensemanager.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget WHERE id = 1")
    fun getBudget(): Flow<BudgetEntity?>

    @Query("SELECT * FROM budget WHERE id = 2")
    fun getDailyLimit(): Flow<BudgetEntity?>

    @Upsert
    suspend fun setBudget(budget: BudgetEntity)
}