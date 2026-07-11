package com.example.personalexpensemanager.domain.validation
import java.math.BigDecimal

fun notEmpty(value: String, errorResId: Int): Int? =
    if (value.isBlank()) errorResId else null

fun maxLength(value: String, max: Int, errorResId: Int): Int? =
    if (value.length > max) errorResId else null

fun positiveAmount(value: BigDecimal?, errorResId: Int): Int? =
    if (value == null || value <= BigDecimal.ZERO) errorResId else null

fun required(value: Any?, errorResId: Int): Int? =
    if (value == null) errorResId else null