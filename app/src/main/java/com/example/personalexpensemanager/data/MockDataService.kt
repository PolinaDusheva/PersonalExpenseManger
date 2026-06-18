package com.example.personalexpensemanager.data

import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Transaction
import java.time.LocalDate
import kotlin.collections.listOf

class MockDataService {

    private val transactions: List<Transaction> = mutableListOf<Transaction>(
    Transaction(id = "t1", title = "Супермаркет", amount = "150", date = LocalDate.of(2026, 11, 6), currency = '€', sign = '-'),
    Transaction(id = "t2", title = "Наем", amount = "660", date = LocalDate.of(2026, 6, 1), currency = '€', sign = '-'),
    Transaction(id = "t3", title = "Заплата", amount = "2500", date = LocalDate.of(2026, 6, 5), currency = '€', sign = '+'),
    Transaction(id = "t4", title = "Интернет", amount = "20", date = LocalDate.of(2026, 6, 10), currency = '€', sign = '-'),
    Transaction(id = "t5", title = "Ресторант", amount = "40", date = LocalDate.of(2026, 6, 9), currency = '€', sign = '-'),
    Transaction(id = "t6", title = "Гориво", amount = "80", date = LocalDate.of(2026, 6, 11), currency = '€', sign = '-'),
    Transaction(id = "t7", title = "Ток", amount = "120", date = LocalDate.of(2026, 6, 12), currency = '€', sign = '-'),
    Transaction(id = "t8", title = "Вода", amount = "35", date = LocalDate.of(2026, 6, 13), currency = '€', sign = '-'),
    Transaction(id = "t9", title = "Фитнес", amount = "50", date = LocalDate.of(2026, 6, 14), currency = '€', sign = '-'),
    Transaction(id = "t10", title = "Бонус", amount = "500", date = LocalDate.of(2026, 6, 15), currency = '€', sign = '+'),
    Transaction(id = "t11", title = "Кафе", amount = "8", date = LocalDate.of(2026, 6, 16), currency = '€', sign = '-'),
    Transaction(id = "t12", title = "Книги", amount = "45", date = LocalDate.of(2026, 6, 17), currency = '€', sign = '-'),
    Transaction(id = "t13", title = "Телефон", amount = "30", date = LocalDate.of(2026, 6, 18), currency = '€', sign = '-'),
    Transaction(id = "t14", title = "Подарък", amount = "100", date = LocalDate.of(2026, 6, 19), currency = '€', sign = '-'),
    Transaction(id = "t15", title = "Хонорар", amount = "750", date = LocalDate.of(2026, 6, 20), currency = '€', sign = '+')
    )

    fun getTransactions(): List<Transaction> {
        return transactions
    }


    fun getCategories(): List<Category> = listOf(
        Category(id = "c1", iconName = "restaurant", name = "Храна", progress = 0.6f, percentage = "60%"),
        Category(id = "c2", iconName = "car", name = "Транспорт", progress = 0.3f, percentage = "30%"),
        Category(id = "c3", iconName = "payments", name = "Сметки", progress = 0.1f, percentage = "10%"),
    )

}