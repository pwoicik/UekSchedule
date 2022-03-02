package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.destinations.ManageActivitiesScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.destinations.ManageGroupsScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun ScheduleScreenScaffold(
    scaffoldState: ScaffoldState,
    swipeEnabled: Boolean,
    isUpdating: Boolean,
    onRefresh: () -> Unit,
    onUpdate: () -> Unit,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    navigator: DestinationsNavigator,
    content: @Composable () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ScheduleScreenTopBar(
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                onManageGroupsButtonClick = {
                    navigator.navigate(ManageGroupsScreenDestination)
                },
                onManageActivitiesButtonClick = {
                    navigator.navigate(ManageActivitiesScreenDestination)
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
