package com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedule.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.features.schedule.presentation.components.ScheduleEntriesListScaffold
import com.github.pwoicik.uekschedule.features.schedule.presentation.components.ScheduleEntriesListScaffoldColors
import com.github.pwoicik.uekschedule.presentation.components.SmallTopBarWithSearchColors
import com.github.pwoicik.uekschedule.resources.R

@Composable
internal fun ScheduleScaffold(
    isSearchButtonEnabled: Boolean,
    searchValue: TextFieldValue,
    onSearchValueChange: (TextFieldValue) -> Unit,
    isFabVisible: Boolean,
    onFabClick: () -> Unit,
    isRefreshing: Boolean,
    onRefreshButtonClick: () -> Unit,
    onMoodleButtonClick: () -> Unit,
    onPreferencesButtonClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    content: @Composable BoxScope.() -> Unit
) {
    var isSearchFieldVisible by rememberSaveable { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    ScheduleEntriesListScaffold(
        title = stringResource(R.string.app_name),
        isSearchFieldVisible = isSearchFieldVisible,
        dismissSearchField = {
            onSearchValueChange(TextFieldValue())
            isSearchFieldVisible = false
        },
        searchValue = searchValue,
        onSearchValueChange = onSearchValueChange,
        onSearchValueClear = {
            onSearchValueChange(TextFieldValue())
            isSearchFieldVisible = false
        },
        isFabVisible = isFabVisible,
        onFabClick = onFabClick,
        isRefreshing = isRefreshing,
        snackbarHostState = snackbarHostState,
        colors = ScheduleEntriesListScaffoldColors.default(
            topBarColors = SmallTopBarWithSearchColors.default(
                titleColor = MaterialTheme.colorScheme.onSecondaryContainer,
                indicatorsColor = MaterialTheme.colorScheme.secondary
            ),
            fabContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            fabContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        actions = {
            IconButton(
                enabled = isSearchButtonEnabled,
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
                        enabled = isSearchButtonEnabled,
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
                        text = { Text(stringResource(R.string.moodle)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_moodle),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        onClick = {
                            onMoodleButtonClick()
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
                        onClick = {
                            isDropdownExpanded = false
                            onPreferencesButtonClick()
                        }
                    )
                }
            }
        },
        content = content
    )
}
