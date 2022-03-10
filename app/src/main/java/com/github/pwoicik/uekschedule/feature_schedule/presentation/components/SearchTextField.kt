package com.github.pwoicik.uekschedule.feature_schedule.presentation.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
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
    leadingIconDescription: String,
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
                contentDescription = leadingIconDescription
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
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    textColor: Color = MaterialTheme.colorScheme.contentColorFor(backgroundColor),
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    placeholderColor: Color = textColor.copy(alpha = 0.7f),
    leadingIconColor: Color = placeholderColor,
    trailingIconColor: Color = placeholderColor,
    focusedIndicatorColor: Color = cursorColor,
    unfocusedIndicatorColor: Color = placeholderColor
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
