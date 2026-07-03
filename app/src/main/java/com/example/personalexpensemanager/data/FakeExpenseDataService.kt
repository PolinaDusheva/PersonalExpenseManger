package com.example.personalexpensemanager.data

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class FakeExpenseDataService: ExpenseDataService {

    private val _transactions = MutableStateFlow(
        listOf(
            Transaction(id = "1", title = "Супермаркет", amount = 150.00, date = LocalDate.of(2026, 5, 6), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "1", description = "Седмично пазаруване", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "2", title = "Наем", amount = 660.00, date = LocalDate.of(2026, 6, 1), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "3", description = "Месечен наем", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "3", title = "Заплата", amount = 2500.00, date = LocalDate.of(2026, 6, 5), currency = Currency.EUR, type = TransactionType.INCOME, categoryId = "1", description = "Месечна заплата", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "4", title = "Интернет", amount = 20.00, date = LocalDate.of(2026, 6, 10), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "3", description = "Интернет сметка", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "5", title = "Ресторант", amount = 40.00, date = LocalDate.of(2026, 6, 9), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "1", description = "Вечеря навън", paymentMethod = PaymentMethod.CASH),
            Transaction(id = "6", title = "Гориво", amount = 80.00, date = LocalDate.of(2026, 6, 11), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "2", description = "Зареждане на колата", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "7", title = "Ток", amount = 120.00, date = LocalDate.of(2026, 6, 12), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "3", description = "Сметка за ток", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "8", title = "Вода", amount = 35.00, date = LocalDate.of(2026, 6, 13), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "3", description = "Сметка за вода", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "9", title = "Книги", amount = 50.00, date = LocalDate.of(2026, 6, 14), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "4", description = "Нова книга", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "10", title = "Бонус", amount = 500.00, date = LocalDate.of(2026, 6, 15), currency = Currency.EUR, type = TransactionType.INCOME, categoryId = "1", description = "Годишен бонус", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "11", title = "Кафе", amount = 8.00, date = LocalDate.of(2026, 6, 16), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "1", description = "Сутрешно кафе", paymentMethod = PaymentMethod.CASH),
            Transaction(id = "12", title = "Гуми", amount = 45.00, date = LocalDate.of(2026, 6, 17), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "2", description = "Смяна на гуми", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "13", title = "Телефон", amount = 30.00, date = LocalDate.of(2026, 6, 18), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "3", description = "Телефонна сметка", paymentMethod = PaymentMethod.CARD),
            Transaction(id = "14", title = "Подарък", amount = 100.00, date = LocalDate.of(2026, 6, 19), currency = Currency.EUR, type = TransactionType.EXPENSE, categoryId = "4", description = "Рожден ден", paymentMethod = PaymentMethod.CASH),
            Transaction(id = "15", title = "Хонорар", amount = 750.00, date = LocalDate.of(2026, 6, 20), currency = Currency.EUR, type = TransactionType.INCOME, categoryId = "1", description = "Допълнителна работа", paymentMethod = PaymentMethod.CARD)
        )
    )
    override val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()
    private val _categories = MutableStateFlow(
        listOf(
            Category(id = "1", iconName = "food", name = "Храна"),
            Category(id = "2", iconName = "transport", name = "Транспорт"),
            Category(id = "3", iconName = "payments", name = "Сметки"),
            Category(id = "4", iconName = "entertainment", name = "Развлечения"),
        )
    )
    override val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    override suspend fun getTransactions(): List<Transaction> {
        delay(1000)
        return _transactions.value
    }

    override suspend fun getCategories(): List<Category> {
        delay(1000)
        return _categories.value
    }

    override suspend fun getTransaction(id: String): Transaction? {
        delay(500)
        return _transactions.value.find { it.id == id }
    }

    override suspend fun addTransaction(transaction: Transaction) {
        _transactions.update { it + transaction }
    }

    override suspend fun addCategory(category: Category) {
        _categories.update { it + category }
    }

    override suspend fun updateCategory(category: Category) {
        _categories.update { list -> list.map { if (it.id == category.id) category else it } }
    }

    override suspend fun deleteCategory(categoryId: String) {
        _categories.update { list -> list.filterNot { it.id == categoryId } }
    }

}