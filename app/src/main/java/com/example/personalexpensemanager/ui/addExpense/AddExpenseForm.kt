package com.example.personalexpensemanager.ui.addExpense

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.ui.components.CategoryDropdown
import com.example.personalexpensemanager.ui.components.appButtons.PrimaryButton
import java.time.LocalDate
import com.example.personalexpensemanager.domain.enums.TransactionType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Brush
import com.example.personalexpensemanager.ui.theme.GradientGraphics
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.personalexpensemanager.ui.components.Headline
import androidx.compose.foundation.clickable
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Modifier.gradientBorderWhenFocused(interactionSource: MutableInteractionSource): Modifier {
    val isFocused by interactionSource.collectIsFocusedAsState()
    return if (isFocused) {
        this.border(
            width = dimensionResource(R.dimen.add_expense_field_border_width),
            brush = GradientGraphics.primaryHorizontal,
            shape = RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius))
        )
    } else {
        this
    }
}
@Composable
fun AddExpenseForm(
    categories: List<Category>,
    isSaving: Boolean,
    onSave: (String, Double, String, LocalDate, String, PaymentMethod, TransactionType) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var date by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var paymentMethod by remember { mutableStateOf(PaymentMethod.CARD) }
    var transactionType by remember { mutableStateOf(TransactionType.EXPENSE) }
    val amountInteractionSource = remember { MutableInteractionSource() }
    val dateInteractionSource = remember { MutableInteractionSource() }
    val descriptionInteractionSource = remember { MutableInteractionSource() }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(top = dimensionResource(R.dimen.padding_small))
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))) {
            Headline(text = stringResource(R.string.add_expense_title))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.add_expense_padding))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.add_expense_field_spacing))
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text(stringResource(R.string.add_expense_amount_label)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number),
                interactionSource = amountInteractionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = dimensionResource(R.dimen.add_expense_field_elevation),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius)))
                    .background(Color.White, RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius)))
                    .gradientBorderWhenFocused(amountInteractionSource),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius))
            )

            CategoryDropdown(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category -> selectedCategory = category },
                includeAll = false
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = date?.format(DateTimeFormatter.ofPattern(stringResource(R.string.date_format_pattern))) ?: "",
                    onValueChange = { },
                    label = { Text(stringResource(R.string.add_expense_date_label)) },
                    readOnly = true,
                    interactionSource = dateInteractionSource,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = dimensionResource(R.dimen.add_expense_field_elevation),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius)))
                        .background(Color.White, RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius)))
                        .gradientBorderWhenFocused(dateInteractionSource),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius))
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                )
            }

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = date
                        ?.atStartOfDay(ZoneId.systemDefault())
                        ?.toInstant()
                        ?.toEpochMilli()
                )
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            }
                            showDatePicker = false
                        }) {
                            Text(stringResource(R.string.action_ok))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text(stringResource(R.string.action_cancel))
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.add_expense_description_label)) },
                interactionSource = descriptionInteractionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = dimensionResource(R.dimen.add_expense_field_elevation),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius)))
                    .background(Color.White, RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius)))
                    .gradientBorderWhenFocused(descriptionInteractionSource),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.add_expense_field_corner_radius))
            )

            Column(modifier = Modifier) {
                Text(stringResource(R.string.payment_method))
                PaymentMethod.entries.forEach { method ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = method == paymentMethod,
                                onClick = { paymentMethod = method }
                            )
                            .padding(vertical = dimensionResource(R.dimen.add_expense_radio_row_padding)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.add_expense_radio_circle_size))
                                .shadow(elevation = dimensionResource(R.dimen.add_expense_radio_circle_elevation),
                                    shape = CircleShape)
                                .background(Color.White, CircleShape)
                                .border(
                                    width = dimensionResource(R.dimen.add_expense_radio_circle_border_width),
                                    brush = if (method == paymentMethod) GradientGraphics.primaryHorizontal
                                    else Brush.linearGradient(
                                        listOf(Color.LightGray, Color.LightGray)
                                    ),
                                    shape = CircleShape
                                )
                                .padding(dimensionResource(R.dimen.add_expense_radio_circle_padding))
                                .clip(CircleShape)
                                .then(
                                    if (method == paymentMethod) Modifier.background(GradientGraphics.primaryHorizontal)
                                    else Modifier
                                )
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.add_expense_radio_label_spacing)))
                        Text(text = if (method == PaymentMethod.CARD) stringResource(R.string.payment_method_card)
                        else stringResource(R.string.payment_method_cash))
                    }
                }
            }
            Column {
                Text(stringResource(R.string.transaction_type))
                TransactionType.entries.forEach { type ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = type == transactionType,
                                onClick = { transactionType = type }
                            )
                            .padding(vertical = dimensionResource(R.dimen.add_expense_radio_row_padding)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.add_expense_radio_circle_size))
                                .shadow(elevation = dimensionResource(R.dimen.add_expense_radio_circle_elevation),
                                    shape = CircleShape)
                                .background(Color.White, CircleShape)
                                .border(
                                    width = dimensionResource(R.dimen.add_expense_radio_circle_border_width),
                                    brush = if (type == transactionType) GradientGraphics.primaryHorizontal
                                    else Brush.linearGradient(
                                        listOf(Color.LightGray, Color.LightGray)
                                    ),
                                    shape = CircleShape
                                )
                                .padding(dimensionResource(R.dimen.add_expense_radio_circle_padding))
                                .clip(CircleShape)
                                .then(
                                    if (type == transactionType) Modifier.background(GradientGraphics.primaryHorizontal)
                                    else Modifier
                                )
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.add_expense_radio_label_spacing)))
                        Text(
                            text = if (type == TransactionType.EXPENSE)
                                stringResource(R.string.transaction_type_expense)
                            else
                                stringResource(R.string.transaction_type_income)
                        )
                    }
                }
            }

            PrimaryButton(
                text = if (isSaving) stringResource(R.string.add_expense_saving) else stringResource(R.string.add_expense_save),
                enabled = !isSaving,
                onClick = {
                    onSave(
                        description,
                        amount.toDoubleOrNull() ?: 0.0,
                        selectedCategory?.id ?: "",
                        date ?: LocalDate.now(),
                        description,
                        paymentMethod,
                        transactionType
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
            onSave = { _, _, _, _, _, _,_ -> }
        )
    }
}