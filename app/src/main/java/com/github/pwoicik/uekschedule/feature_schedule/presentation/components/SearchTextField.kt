package com.github.pwoicik.uekschedule.feature_schedule.presentation.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.github.pwoicik.uekschedule.R

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClearText: () -> Unit = { onValueChange("") },
    placeholder: String? = null,
    singleLine: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    placeholderTextStyle: TextStyle = textStyle
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        placeholder = placeholder?.let {
            { Text(text = it, style = placeholderTextStyle) }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_groups)
            )
        },
        trailingIcon = {
            IconButton(onClick = onClearText) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.clear_search_text)
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onBackground,
            placeholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            backgroundColor = MaterialTheme.colorScheme.background,
            cursorColor = MaterialTheme.colorScheme.primary,
            leadingIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            trailingIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        ),
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}
