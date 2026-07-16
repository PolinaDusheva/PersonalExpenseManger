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
import com.example.personalexpensemanager.domain.enums.TransactionType
import com.example.personalexpensemanager.ui.components.DateField
import com.example.personalexpensemanager.ui.addExpense.components.PaymentMethodSelector
import com.example.personalexpensemanager.ui.addExpense.components.TextField
import com.example.personalexpensemanager.ui.addExpense.components.TransactionTypeSelector
import com.example.personalexpensemanager.ui.components.CategoryDropdown
import com.example.personalexpensemanager.ui.components.GoalDropdown
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.components.appButtons.PrimaryButton
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

@Composable
fun AddExpenseForm(
    state: AddExpenseFormState,
    formErrors: AddExpenseFormErrors,
    categories: List<Category>,
    goals: List<Goal>,
    isSaving: Boolean,
    isEdit: Boolean,
    actions: AddExpenseActions,
    onNavigateToCategories: () -> Unit = {},
    onNavigateToGoals: () -> Unit = {}
) {
    val selectedCategory = categories.find { it.id == state.selectedCategoryId }
    val selectedGoal = goals.find { it.id == state.selectedGoalId }

    var titleWasFocused by remember { mutableStateOf(false) }
    var amountWasFocused by remember { mutableStateOf(false) }
    var descriptionWasFocused by remember { mutableStateOf(false) }
    var categoryDropdownClicked by remember { mutableStateOf(false) }
    var goalDropdownClicked by remember { mutableStateOf(false) }

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
                if (state.step == 1) {
                    PaymentMethodSelector(
                        selected = state.paymentMethod,
                        onSelect = actions::onPaymentMethodChanged
                    )
                    TransactionTypeSelector(
                        selected = state.transactionType,
                        onSelect = actions::onTransactionTypeChanged
                    )
                } else {
                    Column {
                        TextField(
                            value = state.title,
                            onValueChange = actions::onTitleChanged,
                            label = stringResource(R.string.add_expense_title_label),
                            modifier = Modifier.onFocusChanged { focusState ->
                                if (titleWasFocused && !focusState.isFocused) actions.onTitleTouched()
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
                            value = state.amount,
                            onValueChange = actions::onAmountChanged,
                            label = stringResource(R.string.add_expense_amount_label),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.onFocusChanged { focusState ->
                                if (amountWasFocused && !focusState.isFocused) actions.onAmountTouched()
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
                            date = state.date,
                            onDateSelected = { actions.onDateSelected(it) }
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
                            value = state.description,
                            onValueChange = actions::onDescriptionChanged,
                            label = stringResource(R.string.add_expense_description_label),
                            modifier = Modifier.onFocusChanged { focusState ->
                                if (descriptionWasFocused && !focusState.isFocused) actions.onDescriptionTouched()
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

                    if (state.transactionType == TransactionType.TRANSFER) {
                        Column {
                            GoalDropdown(
                                goals = goals,
                                selectedGoal = selectedGoal,
                                onGoalSelected = { goal -> actions.onGoalSelected(goal?.id) },
                                onChipClicked = if (goals.isEmpty()) {
                                    { goalDropdownClicked = true }
                                } else null
                            )
                            if (goals.isEmpty() && goalDropdownClicked) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(R.string.goals_empty_state),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                                    PrimaryButton(
                                        text = stringResource(R.string.goals_add_description),
                                        onClick = onNavigateToGoals
                                    )
                                }
                            }
                        }
                    } else {
                        Column {
                            CategoryDropdown(
                                categories = categories,
                                selectedCategory = selectedCategory,
                                onCategorySelected = { category -> actions.onCategorySelected(category?.id) },
                                onChipClicked = if (categories.isEmpty()) {
                                    { categoryDropdownClicked = true }
                                } else null
                            )
                            if (categories.isEmpty() && categoryDropdownClicked) {
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
                if (state.step == 1) {
                    PrimaryButton(
                        text = stringResource(R.string.next),
                        onClick = { actions.onStepChanged(2) }
                    )
                } else {
                    PrimaryButton(
                        text = stringResource(R.string.category_save),
                        enabled = !isSaving,
                        onClick = actions::onSave
                    )
                    PrimaryButton(
                        text = stringResource(R.string.back),
                        onClick = { actions.onStepChanged(1) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Step 2")
@Composable
fun AddExpenseStepTwoPreview() {
    PersonalExpenseManagerTheme {
        AddExpenseForm(
            state = AddExpenseFormState(
                step = 2,
                title = "Наем",
                amount = "660",
                description = "Месечен наем",
                selectedCategoryId = "1"
            ),
            formErrors = AddExpenseFormErrors(),
            categories = listOf(
                Category("1", "restaurant", "Храна"),
                Category("2", "car", "Транспорт")
            ),
            goals = emptyList(),
            isSaving = false,
            isEdit = false,
            actions = object : AddExpenseActions {}
        )
    }
}