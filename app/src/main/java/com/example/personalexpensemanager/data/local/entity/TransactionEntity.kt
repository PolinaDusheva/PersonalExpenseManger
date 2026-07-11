package com.example.personalexpensemanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.personalexpensemanager.domain.Transaction
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val amount: String,
    val date: Long,
    val currency: String,
    val type: String,
    val categoryId: String?,
    val description: String,
    val paymentMethod: String,
    val goalId: String?
)

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    title = title,
    amount = BigDecimal(amount),
    date = LocalDate.ofEpochDay(date),
    currency = Currency.valueOf(currency),
    type = TransactionType.valueOf(type),
    categoryId = categoryId,
    description = description,
    paymentMethod = PaymentMethod.valueOf(paymentMethod),
    goalId = goalId
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    title = title,
    amount = amount.toPlainString(),
    date = date.toEpochDay(),
    currency = currency.name,
    type = type.name,
    categoryId = categoryId,
    description = description,
    paymentMethod = paymentMethod.name,
    goalId = goalId
)