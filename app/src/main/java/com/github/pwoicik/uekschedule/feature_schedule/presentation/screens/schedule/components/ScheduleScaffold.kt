package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedule.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SmallTopBarWithSearchColors
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList.ScheduleEntriesListScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList.ScheduleEntriesListScaffoldColors
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.LocalBottomBarHeight

@Composable
fun ScheduleScaffold(
    isSearchButtonEnabled: Boolean,
    searchValue: TextFieldValue,
    onSearchValueChange: (TextFieldValue) -> Unit,
    isFabVisible: Boolean,
    onFabClick: () -> Unit,
    isRefreshing: Boolean,
    onRefreshButtonClick: () -> Unit,
    onPreferencesButtonClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    content: @Composable BoxScope.() -> Unit
) {
    var isSearchFieldVisible by rememberSaveable { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val bottomPadding = with(LocalDensity.current) {
        val insets = WindowInsets
        insets.ime.getBottom(this).toDp().coerceAtLeast(
            insets.navigationBars.getBottom(this).toDp() + LocalBottomBarHeight.current
        )
    }
    ScheduleEntriesListScaffold(
        title = stringResource(R.string.app_name),
        isSearchFieldVisible = isSearchFieldVisible,
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
        content = content,
        modifier = Modifier.padding(bottom = bottomPadding)
    )
}
