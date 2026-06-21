package com.example.personalexpensemanager.domain

import java.time.LocalDate

data class Transaction(
    val id: String,
    val title: String,
    val amount: Double,
    val date: LocalDate,
    val currency: Char,
    val sign: Char,
    val categoryId : String
)