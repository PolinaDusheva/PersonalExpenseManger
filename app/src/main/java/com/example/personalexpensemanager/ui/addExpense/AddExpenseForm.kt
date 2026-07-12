package com.example.personalexpensemanager.ui.addExpense

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
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
import androidx.compose.runtime.LaunchedEffect
import com.example.personalexpensemanager.domain.Transaction

@Composable
fun AddExpenseForm(
    categories: List<Category>,
    goals: List<Goal> = emptyList(),
    isSaving: Boolean,
    formErrors: AddExpenseFormErrors,
    existingTransaction: Transaction? = null,
    onTitleChanged: (String) -> Unit,
    onTitleTouched: () -> Unit,
    onAmountChanged: (String) -> Unit,
    onAmountTouched: () -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDescriptionTouched: () -> Unit,
    onDateSelected: (LocalDate?) -> Unit,
    onCategorySelected: () -> Unit,
    onNavigateToCategories: () -> Unit = {},
    onSave: (String, BigDecimal, String?, LocalDate?, String, PaymentMethod, TransactionType, String?) -> Unit
) {
    val isEdit = existingTransaction != null
    var step by remember { mutableIntStateOf(if (isEdit) 2 else 1) }

    var paymentMethod by remember { mutableStateOf(existingTransaction?.paymentMethod ?: PaymentMethod.CARD) }
    var transactionType by remember { mutableStateOf(existingTransaction?.type ?: TransactionType.EXPENSE) }

    var title by remember { mutableStateOf(existingTransaction?.title ?: "") }
    var amount by remember { mutableStateOf(existingTransaction?.amount?.toPlainString() ?: "") }
    var description by remember { mutableStateOf(existingTransaction?.description ?: "") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedGoal by remember { mutableStateOf<Goal?>(null) }
    var date by remember { mutableStateOf(existingTransaction?.date) }

    LaunchedEffect(categories, existingTransaction) {
        if (existingTransaction != null && selectedCategory == null) {
            selectedCategory = categories.find { it.id == existingTransaction.categoryId }
        }
    }

    LaunchedEffect(goals, existingTransaction) {
        if (existingTransaction != null && selectedGoal == null) {
            selectedGoal = goals.find { it.id == existingTransaction.goalId }
        }
    }
    LaunchedEffect(existingTransaction) {
        if (existingTransaction != null) {
            title = existingTransaction.title
            amount = existingTransaction.amount.toPlainString()
            description = existingTransaction.description
            date = existingTransaction.date
            paymentMethod = existingTransaction.paymentMethod
            transactionType = existingTransaction.type
        }
    }

    var titleWasFocused by remember { mutableStateOf(false) }
    var amountWasFocused by remember { mutableStateOf(false) }
    var descriptionWasFocused by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.add_expense),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = dimensionResource(R.dimen.padding_small))
        ) {
            Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal))) {
                Headline(text = stringResource(if (isEdit) R.string.edit_expense_title
                else R.string.add_expense_title))
            }

            Column(
                modifier = Modifier
                    .weight(0.75f)
                    .padding(horizontal = dimensionResource(R.dimen.add_expense_padding))
                    .padding(top = dimensionResource(R.dimen.add_expense_padding))
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.add_expense_field_spacing))
            ) {
                if (step == 1) {
                    PaymentMethodSelector(
                        selected = paymentMethod,
                        onSelect = { paymentMethod = it }
                    )
                    TransactionTypeSelector(
                        selected = transactionType,
                        onSelect = { transactionType = it }
                    )
                } else {
                    Column {
                        TextField(
                            value = title,
                            onValueChange = {
                                title = it
                                onTitleChanged(it)
                            },
                            label = stringResource(R.string.add_expense_title_label),
                            modifier = Modifier.onFocusChanged { focusState ->
                                if (titleWasFocused && !focusState.isFocused) onTitleTouched()
                                titleWasFocused = focusState.isFocused
                            }
                        )
                        if (formErrors.titleTouched && formErrors.titleErrorResId != null) {
                            Text(
                                text = stringResource(formErrors.titleErrorResId),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Column {
                        TextField(
                            value = amount,
                            onValueChange = {
                                amount = it
                                onAmountChanged(it)
                            },
                            label = stringResource(R.string.add_expense_amount_label),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.onFocusChanged { focusState ->
                                if (amountWasFocused && !focusState.isFocused) onAmountTouched()
                                amountWasFocused = focusState.isFocused
                            }
                        )
                        if (formErrors.amountTouched && formErrors.amountErrorResId != null) {
                            Text(
                                text = stringResource(formErrors.amountErrorResId),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Column {
                        DateField(
                            date = date,
                            onDateSelected = {
                                date = it
                                onDateSelected(it)
                            }
                        )
                        if (formErrors.dateTouched && formErrors.dateErrorResId != null) {
                            Text(
                                text = stringResource(formErrors.dateErrorResId),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Column {
                        TextField(
                            value = description,
                            onValueChange = {
                                description = it
                                onDescriptionChanged(it)
                            },
                            label = stringResource(R.string.add_expense_description_label),
                            modifier = Modifier.onFocusChanged { focusState ->
                                if (descriptionWasFocused && !focusState.isFocused) onDescriptionTouched()
                                descriptionWasFocused = focusState.isFocused
                            }
                        )
                        if (formErrors.descriptionTouched && formErrors.descriptionErrorResId != null) {
                            Text(
                                text = stringResource(formErrors.descriptionErrorResId),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    if (transactionType == TransactionType.TRANSFER) {
                        GoalDropdown(
                            goals = goals,
                            selectedGoal = selectedGoal,
                            onGoalSelected = { goal -> selectedGoal = goal }
                        )
                    } else {
                        Column {
                            if (categories.isEmpty() && !isSaving) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(R.string.categories_empty_state),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                                    PrimaryButton(
                                        text = stringResource(R.string.categories_add_title),
                                        onClick = onNavigateToCategories
                                    )
                                }
                            } else {
                                CategoryDropdown(
                                    categories = categories,
                                    selectedCategory = selectedCategory,
                                    onCategorySelected = {
                                        selectedCategory = it
                                        onCategorySelected()
                                    }
                                )
                            }
                            if (formErrors.categoryTouched && formErrors.categoryErrorResId != null) {
                                Text(
                                    text = stringResource(formErrors.categoryErrorResId),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.25f)
                    .padding(horizontal = dimensionResource(R.dimen.add_expense_padding)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                if (step == 1) {
                    PrimaryButton(
                        text = stringResource(R.string.next),
                        onClick = { step = 2 }
                    )
                } else {
                    PrimaryButton(
                        text = stringResource(R.string.category_save),
                        enabled = !isSaving,
                        onClick = {
                            onSave(
                                title,
                                amount.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                                if (transactionType == TransactionType.TRANSFER) null else selectedCategory?.id,
                                date,
                                description,
                                paymentMethod,
                                transactionType,
                                if (transactionType == TransactionType.TRANSFER) selectedGoal?.id else null
                            )
                        }
                    )
                    PrimaryButton(
                        text = stringResource(R.string.back),
                        onClick = { step = 1 }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Step 1")
@Composable
fun AddExpenseStepOnePreview() {
    PersonalExpenseManagerTheme {
        AddExpenseForm(
            categories = listOf(
                Category("1", "restaurant", "Храна"),
                Category("2", "car", "Транспорт")
            ),
            isSaving = false,
            formErrors = AddExpenseFormErrors(),
            onTitleChanged = {},
            onTitleTouched = {},
            onAmountChanged = {},
            onAmountTouched = {},
            onDescriptionChanged = {},
            onDescriptionTouched = {},
            onDateSelected = {},
            onCategorySelected = {},
            onSave = { _, _, _, _, _, _, _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "Step 2 with errors")
@Composable
fun AddExpenseStepTwoErrorsPreview() {
    PersonalExpenseManagerTheme {
        AddExpenseForm(
            categories = listOf(
                Category("1", "restaurant", "Храна"),
                Category("2", "car", "Транспорт")
            ),
            isSaving = false,
            formErrors = AddExpenseFormErrors(
                titleErrorResId = R.string.validation_title_empty,
                amountErrorResId = R.string.validation_amount_required,
                descriptionErrorResId = R.string.validation_description_empty,
                dateErrorResId = R.string.validation_date_required,
                categoryErrorResId = R.string.validation_category_required,
                titleTouched = true,
                amountTouched = true,
                descriptionTouched = true,
                dateTouched = true,
                categoryTouched = true
            ),
            onTitleChanged = {},
            onTitleTouched = {},
            onAmountChanged = {},
            onAmountTouched = {},
            onDescriptionChanged = {},
            onDescriptionTouched = {},
            onDateSelected = {},
            onCategorySelected = {},
            onSave = { _, _, _, _, _, _, _, _ -> }
        )
    }
}