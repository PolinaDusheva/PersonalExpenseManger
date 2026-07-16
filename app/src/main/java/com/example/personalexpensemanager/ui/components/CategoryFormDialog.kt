package com.example.personalexpensemanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.ui.components.appButtons.DialogConfirmButton
import com.example.personalexpensemanager.ui.components.appButtons.DialogDismissButton

@Composable
fun CategoryFormDialog(
    title: String,
    initial: Category?,
    nameErrorResId: Int?,
    nameTouched: Boolean,
    iconErrorResId: Int?,
    onNameChanged: (String) -> Unit,
    onNameFieldTouched: () -> Unit,
    onIconTouched: () -> Unit,
    onIconSelected: (String) -> Unit,
    onConfirm: (name: String, icon: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var icon by remember { mutableStateOf(initial?.iconName ?: "") }
    var wasFocused by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = dimensionResource(R.dimen.category_dialog_max_height))
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        onNameChanged(it)
                    },
                    label = { Text(stringResource(R.string.category_name_label)) },
                    isError = nameTouched && nameErrorResId != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (wasFocused && !focusState.isFocused) {
                                onNameFieldTouched()
                            }
                            wasFocused = focusState.isFocused
                        }
                )
                if (nameTouched && nameErrorResId != null) {
                    Text(
                        text = stringResource(nameErrorResId),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(Modifier.height(dimensionResource(R.dimen.category_dialog_field_spacing)))

                Text(stringResource(R.string.category_icon_label))
                Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))

                IconPickerGrid(
                    selectedIcon = icon,
                    onIconSelected = {
                        icon = it
                        onIconSelected(it)
                    }
                )
                if (iconErrorResId != null) {
                    Text(
                        text = stringResource(iconErrorResId),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            DialogConfirmButton(text = stringResource(R.string.category_save), onClick = {
                if (nameErrorResId != null || iconErrorResId != null) {
                    onNameFieldTouched()
                    onIconTouched()
                } else {
                    onConfirm(name, icon)
                }
            })
        },
        dismissButton = {
            DialogDismissButton(text = stringResource(R.string.action_cancel), onClick = onDismiss)
        }
    )
}