package com.example.personalexpensemanager.ui.addExpense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.ui.components.CategoryDropdown
import com.example.personalexpensemanager.ui.components.appButtons.PrimaryButton
import java.time.LocalDate

import androidx.compose.ui.tooling.preview.Preview
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseForm(
    categories: List<Category>,
    isSaving: Boolean,
    onSave: (String, Double, String, LocalDate, String, PaymentMethod) -> Unit,
    onBack: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var paymentMethod by remember { mutableStateOf(PaymentMethod.CARD) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_expense_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.add_expense_padding))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.add_expense_field_spacing))
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text(stringResource(R.string.add_expense_amount_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            CategoryDropdown(
                categories = categories,
                selectedCategoryId = selectedCategory?.id,
                onCategorySelected = { id ->
                    selectedCategory = categories.find { it.id == id }
                },
                includeAll = false
            )

            OutlinedTextField(
                value = date.toString(),
                onValueChange = { },
                label = { Text(stringResource(R.string.add_expense_date_label)) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.add_expense_description_label)) },
                modifier = Modifier.fillMaxWidth()
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
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = method == paymentMethod,
                            onClick = { paymentMethod = method }
                        )
                        Text(text = if (method == PaymentMethod.CARD) "Card" else "Cash")
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
                        date,
                        description,
                        paymentMethod
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
            onSave = { _, _, _, _, _, _ -> },
            onBack = {}
        )
    }
}