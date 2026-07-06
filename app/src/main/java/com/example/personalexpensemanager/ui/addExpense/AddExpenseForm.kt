package com.example.personalexpensemanager.ui.addExpense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.domain.enums.TransactionType
import com.example.personalexpensemanager.ui.addExpense.components.DateField
import com.example.personalexpensemanager.ui.addExpense.components.PaymentMethodSelector
import com.example.personalexpensemanager.ui.addExpense.components.TextField
import com.example.personalexpensemanager.ui.addExpense.components.TransactionTypeSelector
import com.example.personalexpensemanager.ui.components.CategoryDropdown
import com.example.personalexpensemanager.ui.components.GoalDropdown
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.components.appButtons.PrimaryButton
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun AddExpenseForm(
    categories: List<Category>,
    goals: List<Goal> = emptyList(),
    isSaving: Boolean,
    onSave: (String, BigDecimal, String?, LocalDate, String, PaymentMethod, TransactionType, String?) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedGoal by remember { mutableStateOf<Goal?>(null) }
    var date by remember { mutableStateOf<LocalDate?>(null) }
    var paymentMethod by remember { mutableStateOf(PaymentMethod.CARD) }
    var transactionType by remember { mutableStateOf(TransactionType.EXPENSE) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(top = dimensionResource(R.dimen.padding_small))
    ) {
        Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal))) {
            Headline(text = stringResource(R.string.add_expense_title))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.add_expense_padding))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.add_expense_field_spacing))
        ) {
            TextField(
                value = amount,
                onValueChange = { amount = it },
                label = stringResource(R.string.add_expense_amount_label),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            if (transactionType == TransactionType.TRANSFER) {
                GoalDropdown(
                    goals = goals,
                    selectedGoal = selectedGoal,
                    onGoalSelected = { goal -> selectedGoal = goal }
                )
            } else {
                CategoryDropdown(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category -> selectedCategory = category },
                    includeAll = false
                )
            }

            DateField(
                date = date,
                onDateSelected = { date = it }
            )

            TextField(
                value = description,
                onValueChange = { description = it },
                label = stringResource(R.string.add_expense_description_label)
            )

            PaymentMethodSelector(
                selected = paymentMethod,
                onSelect = { paymentMethod = it }
            )

            TransactionTypeSelector(
                selected = transactionType,
                onSelect = { transactionType = it }
            )

            PrimaryButton(
                text = if (isSaving) stringResource(R.string.add_expense_saving) else stringResource(R.string.add_expense_save),
                enabled = !isSaving,
                onClick = {
                    onSave(
                        description,
                        amount.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        if (transactionType == TransactionType.TRANSFER) null else selectedCategory?.id,
                        date ?: LocalDate.now(),
                        description,
                        paymentMethod,
                        transactionType,
                        if (transactionType == TransactionType.TRANSFER) selectedGoal?.id else null
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddExpenseFormPreview() {
    PersonalExpenseManagerTheme {
        AddExpenseForm(
            categories = listOf(
                Category("1", "restaurant", "Храна"),
                Category("2", "car", "Транспорт")
            ),
            isSaving = false,
            onSave = { _, _, _, _, _, _, _, _ -> }
        )
    }
}