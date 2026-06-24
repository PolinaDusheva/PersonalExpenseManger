package com.example.personalexpensemanager.data

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import kotlinx.coroutines.delay
import java.time.LocalDate

class FakeExpenseDataService: ExpenseDataService {

    private val transactions = mutableListOf<Transaction>(
        Transaction(id = "1", title = "Супермаркет", amount = 150.00, date = LocalDate.of(2026, 11, 6), currency = '€', sign = '-', categoryId = "1", description = "Седмично пазаруване", paymentMethod = PaymentMethod.CARD),
        Transaction(id = "2", title = "Наем", amount = 660.00, date = LocalDate.of(2026, 6, 1), currency = '€', sign = '-', categoryId = "1", description = "Месечен наем", paymentMethod = PaymentMethod.CARD),
        Transaction(id = "3", title = "Заплата", amount = 2500.00, date = LocalDate.of(2026, 6, 5), currency = '€', sign = '+', categoryId = "1", description = "Месечна заплата", paymentMethod = PaymentMethod.CARD),
        Transaction(id = "4", title = "Интернет", amount = 20.00, date = LocalDate.of(2026, 6, 10), currency = '€', sign = '-', categoryId = "1", description = "Интернет сметка", paymentMethod = PaymentMethod.CARD),
        Transaction(id = "5", title = "Ресторант", amount = 40.00, date = LocalDate.of(2026, 6, 9), currency = '€', sign = '-', categoryId = "1", description = "Вечеря навън", paymentMethod = PaymentMethod.CASH),
        Transaction(id = "6", title = "Гориво", amount = 80.00, date = LocalDate.of(2026, 6, 11), currency = '€', sign = '-', categoryId = "1", description = "Зареждане на колата", paymentMethod = PaymentMethod.CARD),
        Transaction(id = "7", title = "Ток", amount = 120.00, date = LocalDate.of(2026, 6, 12), currency = '€', sign = '-', categoryId = "1", description = "Сметка за ток", paymentMethod = PaymentMethod.CARD),
        Transaction(id = "8", title = "Вода", amount = 35.00, date = LocalDate.of(2026, 6, 13), currency = '€', sign = '-', categoryId = "1", description = "Сметка за вода", paymentMethod = PaymentMethod.CARD),
        Transaction(id = "9", title = "Фитнес", amount = 50.00, date = LocalDate.of(2026, 6, 14), currency = '€', sign = '-', categoryId = "1", description = "Месечна карта", paymentMethod = PaymentMethod.CARD),
        Transaction(id = "10", title = "Бонус", amount = 500.00, date = LocalDate.of(2026, 6, 15), currency = '€', sign = '+', categoryId = "1", description = "Годишен бонус", paymentMethod = PaymentMethod.CARD),
        Transaction(id = "11", title = "Кафе", amount = 8.00, date = LocalDate.of(2026, 6, 16), currency = '€', sign = '-', categoryId = "1", description = "Сутрешно кафе", paymentMethod = PaymentMethod.CASH),
        Transaction(id = "12", title = "Книги", amount = 45.00, date = LocalDate.of(2026, 6, 17), currency = '€', sign = '-', categoryId = "1", description = "Учебници", paymentMethod = PaymentMethod.CARD),
        Transaction(id = "13", title = "Телефон", amount = 30.00, date = LocalDate.of(2026, 6, 18), currency = '€', sign = '-', categoryId = "1", description = "Телефонна сметка", paymentMethod = PaymentMethod.CARD),
        Transaction(id = "14", title = "Подарък", amount = 100.00, date = LocalDate.of(2026, 6, 19), currency = '€', sign = '-', categoryId = "1", description = "Рожден ден", paymentMethod = PaymentMethod.CASH),
        Transaction(id = "15", title = "Хонорар", amount = 750.00, date = LocalDate.of(2026, 6, 20), currency = '€', sign = '+', categoryId = "1", description = "Допълнителна работа", paymentMethod = PaymentMethod.CARD)
    )

    private val categories = mutableListOf<Category>(
        Category(id = "1", iconName = "restaurant", name = "Храна"),
        Category(id = "2", iconName = "car", name = "Транспорт"),
        Category(id = "3", iconName = "payments", name = "Сметки"),
    )

    override suspend fun getTransactions(): List<Transaction> {
        delay(1000)
        return transactions

    }

    override suspend fun getCategories(): List<Category>{
        delay(1000)
        return categories
    }

    override fun addTransaction(transaction: Transaction) {
        transactions.add(transaction)
    }

    override suspend fun addCategory(category: Category) {
        categories.add(category)
    }

    override suspend fun updateCategory(category: Category){
        val index = categories.indexOfFirst { it.id == category.id }
        if (index != -1) categories[index] = category
    }

    override suspend fun deleteCategory(categoryId: String) {
        categories.removeAll { it.id == categoryId }
    }

}