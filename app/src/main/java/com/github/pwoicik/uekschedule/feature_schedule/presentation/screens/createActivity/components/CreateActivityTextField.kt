package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.createActivity.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.github.pwoicik.uekschedule.presentation.components.FormTextField

@Composable
fun CreateActivityTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    isLast: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    FormTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = label,
        placeholder = placeholder,
        isError = isRequired && value.isBlank(),
        keyboardOptions = KeyboardOptions(
            imeAction = if (isLast) { ImeAction.Done } else ImeAction.Next,
            keyboardType = keyboardType
        ),
        modifier = modifier
    )
}
