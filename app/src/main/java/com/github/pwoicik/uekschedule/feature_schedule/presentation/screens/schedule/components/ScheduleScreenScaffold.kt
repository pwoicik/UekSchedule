package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedule.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.ManageActivitiesScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.ManageGroupsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun ScheduleScreenScaffold(
    scaffoldState: ScaffoldState,
    refreshEnabled: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onUpdate: () -> Unit,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    navigator: DestinationsNavigator,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ScheduleScreenTopBar(
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                refreshEnabled = refreshEnabled,
                isRefreshing = isRefreshing,
                onRefreshButtonClick = onRefresh,
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
                            enabled = !isRefreshing,
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
        },
        content = content
    )
}
