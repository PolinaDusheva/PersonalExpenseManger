package com.example.personalexpensemanager.data.local

import androidx.room.TypeConverter
import com.example.personalexpensemanager.domain.enums.Currency
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? = date?.toEpochDay()

    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? = epochDay?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun fromBigDecimal(amount: BigDecimal): String = amount.toPlainString()

    @TypeConverter
    fun toBigDecimal(amount: String): BigDecimal = BigDecimal(amount)

    @TypeConverter
    fun fromCurrency(currency: Currency): String = currency.name

    @TypeConverter
    fun toCurrency(currency: String): Currency = Currency.valueOf(currency)

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(type: String): TransactionType = TransactionType.valueOf(type)

    @TypeConverter
    fun fromPaymentMethod(method: PaymentMethod): String = method.name

    @TypeConverter
    fun toPaymentMethod(method: String): PaymentMethod = PaymentMethod.valueOf(method)
}