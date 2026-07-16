package com.example.personalexpensemanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.personalexpensemanager.data.local.dao.BudgetDao
import com.example.personalexpensemanager.data.local.dao.CategoryDao
import com.example.personalexpensemanager.data.local.dao.GoalDao
import com.example.personalexpensemanager.data.local.dao.TransactionDao
import com.example.personalexpensemanager.data.local.entity.CategoryEntity
import com.example.personalexpensemanager.data.local.entity.GoalEntity
import com.example.personalexpensemanager.data.local.entity.TransactionEntity
import com.example.personalexpensemanager.data.local.entity.BudgetEntity
@Database(
    entities = [CategoryEntity::class, TransactionEntity::class, GoalEntity::class, BudgetEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun goalDao(): GoalDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "personal_expense_manager.db"
                ).fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}