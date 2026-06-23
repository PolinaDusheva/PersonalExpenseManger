package com.example.personalexpensemanager.ui.addExpense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.enums.PaymentMethod
import com.example.personalexpensemanager.ui.components.CategoryDropdown
import java.time.LocalDate

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
                title = { Text("Нов разход", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Сума") },
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
                label = { Text("Дата") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth()
            )
//ТODO
//            DropdownField(
//                label = "Начин на плащане",
//                value = paymentMethod,
//                options = paymentMethods,
//                onOptionSelected = { paymentMethod = it }
//            )

            Button(
                onClick = {
                    onSave(
                        description,
                        amount.toDoubleOrNull() ?: 0.0,
                        selectedCategory?.id ?: "",
                        date,
                        description,
                        paymentMethod
                    )
                },
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isSaving) "Записва се..." else "Запази")
            }
        }
    }
}