package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Goal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDropdown(
    goals: List<Goal>,
    selectedGoal: Goal?,
    onGoalSelected: (Goal?) -> Unit,
    placeholder: String = stringResource(R.string.goal_dropdown_placeholder),
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val label = selectedGoal?.title ?: placeholder

    Box(modifier = modifier) {
        TransactionFilterChip(
            text = label,
            selected = selectedGoal != null,
            onClick = { expanded = true },
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = if (selectedGoal != null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            goals.forEach { goal ->
                DropdownMenuItem(
                    text = { Text(goal.title) },
                    onClick = {
                        onGoalSelected(goal)
                        expanded = false
                    }
                )
            }
        }
    }
}