package com.example.personalexpensemanager.ui.statistics.components

import java.time.DayOfWeek
import java.time.LocalDate

enum class PeriodType { TODAY, WEEK, MONTH, CUSTOM }

data class Period(
    val type: PeriodType,
    val start: LocalDate,
    val end: LocalDate
) {
    companion object {
        fun today(): Period {
            val now = LocalDate.now()
            return Period(PeriodType.TODAY, now, now)
        }

        fun thisWeek(): Period {
            val now = LocalDate.now()
            return Period(PeriodType.WEEK, now.with(DayOfWeek.MONDAY), now)
        }

        fun thisMonth(): Period {
            val now = LocalDate.now()
            return Period(PeriodType.MONTH, now.withDayOfMonth(1), now)
        }

        fun custom(start: LocalDate, end: LocalDate): Period =
            Period(PeriodType.CUSTOM, start, end)
    }
}