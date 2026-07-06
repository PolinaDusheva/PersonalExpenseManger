package com.example.personalexpensemanager.domain

import java.math.BigDecimal
import java.time.LocalDate

data class Goal(
    val id: String,
    val title: String,
    val targetAmount: BigDecimal,
    val deadline: LocalDate? = null
)