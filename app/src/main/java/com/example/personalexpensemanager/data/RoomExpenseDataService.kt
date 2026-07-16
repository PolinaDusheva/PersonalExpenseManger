package com.example.personalexpensemanager.data
import com.example.personalexpensemanager.data.local.dao.BudgetDao
import com.example.personalexpensemanager.data.local.dao.CategoryDao
import com.example.personalexpensemanager.data.local.dao.GoalDao
import com.example.personalexpensemanager.data.local.dao.TransactionDao
import com.example.personalexpensemanager.data.local.entity.BudgetEntity
import com.example.personalexpensemanager.data.local.entity.toDomain
import com.example.personalexpensemanager.data.local.entity.toEntity
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.domain.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import kotlin.random.Random


class RoomExpenseDataService (
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
    private val goalDao: GoalDao,
    private val budgetDao: BudgetDao,
    scope: CoroutineScope
): IExpenseDataService{

    private fun randomErrorThrow(operation: String) {
        if (Random.nextFloat() < 0.3f) {
            throw RuntimeException("Симулирана грешка: $operation")
        }
    }

    override val categories: StateFlow<List<Category>> =
        categoryDao.observeAll()
            .map { list -> list.map { it.toDomain() } }
            .stateIn(scope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val transactions: StateFlow<List<Transaction>> =
        transactionDao.observeAll()
            .map { list -> list.map { it.toDomain() } }
            .stateIn(scope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val goals: StateFlow<List<Goal>> =
        goalDao.observeAll()
            .map { list -> list.map { it.toDomain() } }
            .stateIn(scope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val monthlyBudget: StateFlow<BigDecimal?> =
        budgetDao.getBudget()
            .map { it?.amount }
            .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    override val dailyLimit: StateFlow<BigDecimal?> =
        budgetDao.getDailyLimit()
            .map { it?.amount }
            .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    override suspend fun getCategories(): List<Category>{
        randomErrorThrow("getCategories")
        return categoryDao.getAll().map { it.toDomain() }
    }

    override suspend fun getTransactions(): List<Transaction> {
        randomErrorThrow("getTransactions")
        return transactionDao.getAll().map { it.toDomain() }
    }
    override suspend fun getTransactionsOrderedByDate(): List<Transaction> {
        randomErrorThrow("getTransactions")
        return transactionDao.getAllByDateDesc().map { it.toDomain() }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        randomErrorThrow("updateTransaction")
        transactionDao.upsert(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transactionId: String) {
        randomErrorThrow("deleteTransaction")
        transactionDao.deleteById(transactionId)
    }

    override suspend fun getTransaction(id: String): Transaction? {
        randomErrorThrow("getTransaction")
        return transactionDao.getById(id)?.toDomain()
    }

    override suspend fun addTransaction(transaction: Transaction) {
        randomErrorThrow("addTransaction")
        transactionDao.upsert(transaction.toEntity())
    }

    override suspend fun addCategory(category: Category) {
        randomErrorThrow("addCategory")
        categoryDao.upsert(category.toEntity())
    }

    override suspend fun updateCategory(category: Category) {
        randomErrorThrow("updateCategory")
        categoryDao.upsert(category.toEntity())
    }

    override suspend fun deleteCategory(categoryId: String) {
        randomErrorThrow("deleteCategory")
        categoryDao.deleteById(categoryId)
    }

    override suspend fun addGoal(goal: Goal) {
        randomErrorThrow("addGoal")
        goalDao.upsert(goal.toEntity())
    }

    override suspend fun updateGoal(goal: Goal) {
        randomErrorThrow("updateGoal")
        goalDao.upsert(goal.toEntity())
    }

    override suspend fun deleteGoal(goalId: String) {
        randomErrorThrow("deleteGoal")
        goalDao.deleteById(goalId)
    }

    override suspend fun setMonthlyBudget(amount: BigDecimal) {
        budgetDao.setBudget(BudgetEntity(amount = amount))
    }

    override suspend fun setDailyLimit(amount: BigDecimal) {
        budgetDao.setBudget(BudgetEntity(id = BudgetEntity.ID_DAILY_LIMIT, amount = amount))
    }
}