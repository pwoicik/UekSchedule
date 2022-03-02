package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.ui.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun ScheduleScreenTopBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    refreshEnabled: Boolean,
    isRefreshing: Boolean,
    onRefreshButtonClick: () -> Unit,
    onManageGroupsButtonClick: () -> Unit,
    onManageActivitiesButtonClick: () -> Unit,
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isInputMode by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        TopAppBar(
            contentPadding = rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.statusBars,
                additionalStart = 8.dp
            ),
            title = {
                Text(stringResource(R.string.app_name))
            },
            actions = {
                if (refreshEnabled) {
                    val spin by rememberInfiniteTransition().animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(tween(
                            durationMillis = 1000,
                            easing = LinearEasing
                        ))
                    )
                    IconButton(
                        enabled = !isRefreshing,
                        onClick = onRefreshButtonClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = stringResource(R.string.refresh_data),
                            modifier = Modifier.rotate(if (isRefreshing) -spin else 0f)
                        )
                    }
                }
                IconButton(onClick = { isInputMode = true }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(
                            R.string.search_entry
                        )
                    )
                }
                IconButton(
                    onClick = { isDropdownExpanded = !isDropdownExpanded }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.manage_groups)
                    )
                }
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        onClick = onManageGroupsButtonClick
                    ) {
                        Text(stringResource(R.string.your_groups))
                    }
                    Divider(modifier = Modifier.padding(horizontal = 8.dp))
                    DropdownMenuItem(
                        onClick = onManageActivitiesButtonClick
                    ) {
                        Text(stringResource(R.string.other_activities))
                    }
                }
            }
        )

        AnimatedVisibility(
            visible = isInputMode,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SearchTopBar(
                value = searchText,
                onValueChange = onSearchTextChange,
                onDismiss = {
                    onSearchTextChange("")
                    isInputMode = false
                }
            )
        }
    }
}

@Composable
fun SearchTopBar(
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        color = MaterialTheme.colors.primarySurface,
        elevation = AppBarDefaults.TopAppBarElevation,
        modifier = Modifier
            .fillMaxSize()
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            ),
            placeholder = {
                Text(stringResource(R.string.entry_search_placeholder))
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onValueChange("")
                        onDismiss()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.cancel_search)
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .focusRequester(focusRequester)
        )
    }
}
