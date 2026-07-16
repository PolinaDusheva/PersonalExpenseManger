package com.example.personalexpensemanager.ui.statistics.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.ui.addExpense.components.TransactionFilterChip
import com.example.personalexpensemanager.ui.components.AppDatePickerDialog
import com.example.personalexpensemanager.ui.components.appDatePickerColors
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodFilter(
    selectedType: PeriodType,
    onPeriodSelected: (Period) -> Unit,
    modifier: Modifier = Modifier
) {
    var showRangePicker by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        TransactionFilterChip(
            text = stringResource(R.string.period_today),
            selected = selectedType == PeriodType.TODAY,
            onClick = { onPeriodSelected(Period.today()) }
        )
        TransactionFilterChip(
            text = stringResource(R.string.period_week),
            selected = selectedType == PeriodType.WEEK,
            onClick = { onPeriodSelected(Period.thisWeek()) }
        )
        TransactionFilterChip(
            text = stringResource(R.string.period_month),
            selected = selectedType == PeriodType.MONTH,
            onClick = { onPeriodSelected(Period.thisMonth()) }
        )
        TransactionFilterChip(
            text = stringResource(R.string.period_custom),
            selected = selectedType == PeriodType.CUSTOM,
            onClick = { showRangePicker = true }
        )
    }

    if (showRangePicker) {
        val rangeState = rememberDateRangePickerState()
        AppDatePickerDialog(
            onDismissRequest = { showRangePicker = false },
            onConfirm = {
                val startMillis = rangeState.selectedStartDateMillis
                val endMillis = rangeState.selectedEndDateMillis
                if (startMillis != null && endMillis != null) {
                    onPeriodSelected(
                        Period.custom(
                            start = millisToDate(startMillis),
                            end = millisToDate(endMillis)
                        )
                    )
                }
                showRangePicker = false
            },
            confirmText = stringResource(R.string.action_ok),
            dismissText = stringResource(R.string.action_cancel)
        ) {
            DateRangePicker(
                state = rangeState,
                modifier = Modifier.weight(1f),
                colors = appDatePickerColors()
            )
        }
    }
}

private fun millisToDate(millis: Long): LocalDate =
    Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()