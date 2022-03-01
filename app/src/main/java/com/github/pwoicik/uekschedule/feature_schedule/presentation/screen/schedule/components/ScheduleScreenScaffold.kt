package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.Screen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule.ScheduleScreenViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ScheduleScreenScaffold(
    scaffoldState: ScaffoldState,
    swipeEnabled: Boolean,
    isUpdating: Boolean,
    onRefresh: () -> Unit,
    onUpdate: () -> Unit,
    navController: NavController,
    content: @Composable () -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars,
                    additionalStart = 8.dp
                ),
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {
                            isDropdownExpanded = !isDropdownExpanded
                        }
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
                            onClick = {
                                navController.navigate(Screen.ManageGroupsScreen.route)
                            }
                        ) {
                            Text(stringResource(R.string.your_groups))
                        }
                        Divider(modifier = Modifier.padding(horizontal = 8.dp))
                        DropdownMenuItem(
                            onClick = {
                                navController.navigate(Screen.ManageActivitiesScreen.route)
                            }
                        ) {
                            Text(stringResource(R.string.other_activities))
                        }
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = it) { snackbarData ->
                Snackbar(
                    action = {
                        IconButton(
                            enabled = !isUpdating,
                            onClick = onUpdate
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.retry)
                            )
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(snackbarData.message)
                }
            }
        }
    ) { innerPadding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isUpdating),
            swipeEnabled = swipeEnabled,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            content = content
        )
    }
}
