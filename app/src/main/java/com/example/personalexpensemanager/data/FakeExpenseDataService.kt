package com.example.personalexpensemanager.data

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import kotlinx.coroutines.delay
import java.time.LocalDate

class FakeExpenseDataService: ExpenseDataService {

    private val transactions = mutableListOf<Transaction>(
    Transaction(id = "1", title = "Супермаркет", amount = 150.00, date = LocalDate.of(2026, 11, 6), currency = '€', sign = '-', categoryId = "1"),
    Transaction(id = "2", title = "Наем", amount = 660.00, date = LocalDate.of(2026, 6, 1), currency = '€', sign = '-', categoryId = "1"),
    Transaction(id = "3", title = "Заплата", amount = 2500.00, date = LocalDate.of(2026, 6, 5), currency = '€', sign = '+',categoryId = "1"),
    Transaction(id = "4", title = "Интернет", amount = 20.00, date = LocalDate.of(2026, 6, 10), currency = '€', sign = '-',categoryId = "1"),
    Transaction(id = "5", title = "Ресторант", amount = 40.00, date = LocalDate.of(2026, 6, 9), currency = '€', sign = '-',categoryId = "1"),
    Transaction(id = "6", title = "Гориво", amount = 80.00, date = LocalDate.of(2026, 6, 11), currency = '€', sign = '-',categoryId = "1"),
    Transaction(id = "7", title = "Ток", amount = 120.00, date = LocalDate.of(2026, 6, 12), currency = '€', sign = '-',categoryId = "1"),
    Transaction(id = "8", title = "Вода", amount = 35.00, date = LocalDate.of(2026, 6, 13), currency = '€', sign = '-',categoryId = "1"),
    Transaction(id = "9", title = "Фитнес", amount = 50.00, date = LocalDate.of(2026, 6, 14), currency = '€', sign = '-',categoryId = "1"),
    Transaction(id = "10", title = "Бонус", amount = 500.00, date = LocalDate.of(2026, 6, 15), currency = '€', sign = '+',categoryId = "1"),
    Transaction(id = "11", title = "Кафе", amount = 8.00, date = LocalDate.of(2026, 6, 16), currency = '€', sign = '-',categoryId = "1"),
    Transaction(id = "12", title = "Книги", amount = 45.00, date = LocalDate.of(2026, 6, 17), currency = '€', sign = '-',categoryId = "1"),
    Transaction(id = "13", title = "Телефон", amount = 30.00, date = LocalDate.of(2026, 6, 18), currency = '€', sign = '-',categoryId = "1"),
    Transaction(id = "14", title = "Подарък", amount = 100.00, date = LocalDate.of(2026, 6, 19), currency = '€', sign = '-',categoryId = "1"),
    Transaction(id = "15", title = "Хонорар", amount = 750.00, date = LocalDate.of(2026, 6, 20), currency = '€', sign = '+',categoryId = "1")
    )

    private val categories = mutableListOf<Category>(
        Category(id = "1", iconName = "restaurant", name = "Храна", progress = 0.6f, percentage = "60%"),
        Category(id = "2", iconName = "car", name = "Транспорт", progress = 0.3f, percentage = "30%"),
        Category(id = "3", iconName = "payments", name = "Сметки", progress = 0.1f, percentage = "10%"),
    )

    override suspend fun getTransactions(): List<Transaction> {
        delay(1000)
        return transactions

    }

    override suspend fun getCategories(): List<Category>{
        delay(1000)
        return categories
    }



}