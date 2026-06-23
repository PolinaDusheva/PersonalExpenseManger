package com.example.personalexpensemanager.domain

import com.example.personalexpensemanager.domain.enums.PaymentMethod
import java.time.LocalDate

data class Transaction(
    val id: String,
    val title: String,
    val amount: Double,
    val date: LocalDate,
    val currency: Char,
    val sign: Char,
    val categoryId : String,
    val description: String,
    val paymentMethod: PaymentMethod
)