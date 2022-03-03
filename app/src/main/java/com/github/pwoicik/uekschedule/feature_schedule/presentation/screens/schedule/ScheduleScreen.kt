package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedule

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedule.components.ScheduleEntriesList
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedule.components.ScheduleScreenScaffold
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Destination(
    start = true
)
@Composable
fun ScheduleScreen(
    navigator: DestinationsNavigator,
    viewModel: ScheduleScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state

    val timeNow by viewModel.timeFlow.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val snackbarMessage = stringResource(R.string.couldnt_connect)

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ScheduleScreenViewModel.UiEvent.ShowSnackbar -> {
                    launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = snackbarMessage,
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                }
                ScheduleScreenViewModel.UiEvent.HideSnackbar -> {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
        }
    }

    ScheduleScreenScaffold(
        scaffoldState = scaffoldState,
        refreshEnabled = state.hasSavedGroups,
        isRefreshing = state.isRefreshing,
        onRefresh = viewModel::refreshClasses,
        onUpdate = viewModel::refreshClasses,
        searchText = state.searchText,
        onSearchTextChange = viewModel::updateSearchResults,
        navigator = navigator
    ) { innerPadding ->
        Crossfade(
            targetState = state.entries.isEmpty(),
            modifier = Modifier.padding(innerPadding)
        ) { isEmpty ->
            when (isEmpty) {
                true -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            if (state.hasSavedGroups)
                                stringResource(R.string.no_classes_message)
                            else
                                stringResource(R.string.no_saved_groups)
                        )
                    }
                }
                false -> {
                    val filteredEntries by derivedStateOf {
                        state.entries.filter { entriesByDate ->
                            val matchesName =
                                entriesByDate.name.contains(state.searchText, ignoreCase = true)
                            val matchesTeacher =
                                entriesByDate.teachers?.any {
                                    it.contains(state.searchText, ignoreCase = true)
                                } ?: false

                            matchesName || matchesTeacher
                        }.groupBy(ScheduleEntry::startDate)
                    }
                    ScheduleEntriesList(
                        scheduleEntries = filteredEntries,
                        timeNow = timeNow
                    )
                }
            }
        }
    }
}
