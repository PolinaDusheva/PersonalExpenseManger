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
    val amount: BigDecimal,
    val date: LocalDate,
    val currency: Currency,
    val type: TransactionType,
    val categoryId: String?,
    val description: String,
    val paymentMethod: PaymentMethod,
    val goalId: String?
)

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    title = title,
    amount = amount,
    date = date,
    currency = currency,
    type = type,
    categoryId = categoryId,
    description = description,
    paymentMethod = paymentMethod,
    goalId = goalId
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    title = title,
    amount = amount,
    date = date,
    currency = currency,
    type = type,
    categoryId = categoryId,
    description = description,
    paymentMethod = paymentMethod,
    goalId = goalId
)