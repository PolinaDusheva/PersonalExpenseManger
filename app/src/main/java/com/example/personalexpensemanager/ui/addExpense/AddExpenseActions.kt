package com.example.personalexpensemanager.ui.addExpense

import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import java.time.LocalDate

interface AddExpenseActions {
    fun onTitleChanged(value: String) {}
    fun onTitleTouched() {}
    fun onAmountChanged(value: String) {}
    fun onAmountTouched() {}
    fun onDescriptionChanged(value: String) {}
    fun onDescriptionTouched() {}
    fun onDateSelected(date: LocalDate?) {}
    fun onCategorySelected(categoryId: String?) {}
    fun onGoalSelected(goalId: String?) {}
    fun onPaymentMethodChanged(method: PaymentMethod) {}
    fun onTransactionTypeChanged(type: TransactionType) {}
    fun onStepChanged(step: Int) {}
    fun onSave() {}
}