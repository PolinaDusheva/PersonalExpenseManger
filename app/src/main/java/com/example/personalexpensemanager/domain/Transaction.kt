package com.example.personalexpensemanager.domain

import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

data class Transaction(
    val id: String,
    val title: String,
    val amount: BigDecimal,
    val date: LocalDate,
    val currency: Currency,
    val type: TransactionType,
    val categoryId: String? = null,
    val description: String,
    val paymentMethod: PaymentMethod,
    val goalId: String? = null
)
