package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.schedule.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SmallTopBarWithSearch

@Composable
fun ScheduleDestinationTopBar(
    hasGroups: Boolean,
    searchValue: TextFieldValue,
    onSearchValueChange: (TextFieldValue) -> Unit,
    onRefreshButtonClick: () -> Unit,
    onPreferencesButtonClick: () -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isSearchFieldVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(hasGroups) {
        isSearchFieldVisible = false
    }

    SmallTopBarWithSearch(
        title = { Text(stringResource(R.string.app_name)) },
        isSearchFieldVisible = isSearchFieldVisible,
        searchValue = searchValue,
        onSearchValueChange = { onSearchValueChange(it) },
        onSearchValueClear = {
            onSearchValueChange(TextFieldValue())
            isSearchFieldVisible = false
        },
        actions = {
            IconButton(
                enabled = hasGroups,
                onClick = { isSearchFieldVisible = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_entry)
                )
            }
            IconButton(onClick = { isDropdownExpanded = !isDropdownExpanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.more)
                )
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        enabled = hasGroups,
                        text = { Text(stringResource(R.string.refresh_data)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.refresh_data)
                            )
                        },
                        onClick = {
                            onRefreshButtonClick()
                            isDropdownExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.preferences)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(R.string.preferences)
                            )
                        },
                        onClick = onPreferencesButtonClick
                    )
                }
            }
        }
    )
}
