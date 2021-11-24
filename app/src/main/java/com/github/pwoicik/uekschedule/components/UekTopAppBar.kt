package com.github.pwoicik.uekschedule.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.pwoicik.uekschedule.R

@Composable
fun UekTopAppBar(
    isExpanded: Boolean,
    toggleDarkMode: () -> Unit,
    toggleDropdown: () -> Unit,
    onDismiss: () -> Unit,
    onEditGroups: () -> Unit
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.app_name))
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        actions = {
            IconButton(onClick = toggleDarkMode) {
                Icon(
                    if (MaterialTheme.colors.isLight)
                        Icons.Filled.DarkMode
                    else
                        Icons.Filled.LightMode,
                    contentDescription = ""
                )
            }
            IconButton(
                onClick = toggleDropdown
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = onDismiss
                ) {
                    DropdownMenuItem(onClick = onEditGroups) {
                        Text(stringResource(R.string.edit_gropus))
                    }
                }
            }
        }
    )
}

