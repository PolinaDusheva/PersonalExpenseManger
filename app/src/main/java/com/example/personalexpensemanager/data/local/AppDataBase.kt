package com.example.personalexpensemanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.personalexpensemanager.data.local.dao.CategoryDao
import com.example.personalexpensemanager.data.local.dao.GoalDao
import com.example.personalexpensemanager.data.local.dao.TransactionDao
import com.example.personalexpensemanager.data.local.entity.CategoryEntity
import com.example.personalexpensemanager.data.local.entity.GoalEntity
import com.example.personalexpensemanager.data.local.entity.TransactionEntity

@Database(
    entities = [CategoryEntity::class, TransactionEntity::class, GoalEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "personal_expense_manager.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}