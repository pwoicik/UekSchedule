package com.github.pwoicik.uekschedule.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.resources.R

@OptIn(ExperimentalMaterial3Api::class)
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
                contentDescription = null
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldDefaults.searchTextFieldColors(
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    focusedIndicatorColor: Color = cursorColor,
    unfocusedIndicatorColor: Color = placeholderColor
) = textFieldColors(
    textColor = textColor,
    placeholderColor = placeholderColor,
    containerColor = backgroundColor,
    cursorColor = cursorColor,
    focusedIndicatorColor = focusedIndicatorColor,
    unfocusedIndicatorColor = unfocusedIndicatorColor
)
