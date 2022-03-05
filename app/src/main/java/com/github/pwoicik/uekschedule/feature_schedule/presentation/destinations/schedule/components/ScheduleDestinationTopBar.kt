package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.schedule.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SearchTextField
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun ScheduleDestinationTopBar(
    searchText: TextFieldValue,
    onSearchTextChange: (TextFieldValue) -> Unit,
    onRefreshButtonClick: () -> Unit,
    onPreferencesButtonClick: () -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    var isSearchFieldVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Surface {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .animateContentSize()
        ) {
            SmallTopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onClick = { isSearchFieldVisible = true }) {
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
            AnimatedVisibility(
                visible = isSearchFieldVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                SearchTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    onClearText = {
                        onSearchTextChange(TextFieldValue())
                        isSearchFieldVisible = false
                    },
                    placeholder = stringResource(R.string.entry_search_placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .focusRequester(focusRequester)
                )
            }
        }
    }
}
