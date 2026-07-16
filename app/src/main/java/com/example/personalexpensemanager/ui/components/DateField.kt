package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.addExpense.components.TextField
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    date: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = date?.format(DateTimeFormatter.ofPattern(stringResource(R.string.date_format_pattern)))
                ?: "",
            onValueChange = {},
            label = stringResource(R.string.add_expense_date_label),
            readOnly = true
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
        AppDatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onConfirm = {
                datePickerState.selectedDateMillis?.let { millis ->
                    onDateSelected(Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate())
                }
                showDatePicker = false
            }
        ) {
            DatePicker(state = datePickerState, colors = appDatePickerColors())
        }
    }
}