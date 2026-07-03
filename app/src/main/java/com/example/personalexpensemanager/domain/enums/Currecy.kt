package com.example.personalexpensemanager.domain.enums

enum class Currency {
    EUR, USD, BGN
}

val Currency.symbol: String
    get() = when (this) {
        Currency.EUR -> "€"
        Currency.USD -> "$"
        Currency.BGN -> "лв."
    }