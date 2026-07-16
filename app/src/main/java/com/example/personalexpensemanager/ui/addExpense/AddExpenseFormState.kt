package com.example.personalexpensemanager.ui.addExpense

import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import java.time.LocalDate

data class AddExpenseFormState(
    val step: Int = 1,
    val title: String = "",
    val amount: String = "",
    val description: String = "",
    val date: LocalDate? = null,
    val paymentMethod: PaymentMethod = PaymentMethod.CARD,
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val selectedCategoryId: String? = null,
    val selectedGoalId: String? = null
)