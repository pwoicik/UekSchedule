package com.github.pwoicik.uekschedule.feature_schedule.presentation.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.R

@Composable
fun SearchTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    onClearText: () -> Unit = { onValueChange(TextFieldValue("")) },
    placeholder: String? = null,
    singleLine: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    colors: TextFieldColors = TextFieldDefaults.searchTextFieldColors(),
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
        colors = colors,
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}

@Composable
fun TextFieldDefaults.searchTextFieldColors(
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    placeholderColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    leadingIconColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
    trailingIconColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
    focusedIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
) = textFieldColors(
    textColor = textColor,
    placeholderColor = placeholderColor,
    backgroundColor = backgroundColor,
    cursorColor = cursorColor,
    leadingIconColor = leadingIconColor,
    trailingIconColor = trailingIconColor,
    focusedIndicatorColor = focusedIndicatorColor,
    unfocusedIndicatorColor = unfocusedIndicatorColor
)
