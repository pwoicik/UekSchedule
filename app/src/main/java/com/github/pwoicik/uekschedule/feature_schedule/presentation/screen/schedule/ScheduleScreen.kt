package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule.components.ScheduleEntriesList
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule.components.ScheduleScreenScaffold
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
        swipeEnabled = state.hasSavedGroups,
        isUpdating = state.isUpdating,
        onRefresh = viewModel::updateClasses,
        onUpdate = viewModel::updateClasses,
        searchText = state.searchText,
        onSearchTextChange = viewModel::updateSearchResults,
        navigator = navigator
    ) {
        Crossfade(state.entries.isEmpty()) { isEmpty ->
            when (isEmpty) {
                true -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
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
                        state.entries.filter { entry ->
                            val matchesName =
                                entry.name.contains(state.searchText, ignoreCase = true)
                            val matchesTeacher =
                                entry.teachers?.any {
                                    it.contains(state.searchText, ignoreCase = true)
                                } ?: false

                            matchesName || matchesTeacher
                        }
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
