package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule.components.ScheduleEntriesList
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule.components.ScheduleScreenScaffold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ScheduleScreen(
    navController: NavController,
    viewModel: ScheduleScreenViewModel = hiltViewModel()
) {
    val scheduleEntries by viewModel.scheduleEntries.collectAsState(null)
    val isUpdating by viewModel.isUpdating

    val timeNow by viewModel.timeFlow.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val snackbarMessage = stringResource(R.string.couldnt_connect)

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ScheduleScreenViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = snackbarMessage,
                        duration = SnackbarDuration.Indefinite
                    )
                }
                ScheduleScreenViewModel.UiEvent.HideSnackbar -> {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
        }
    }

    ScheduleScreenScaffold(
        scaffoldState = scaffoldState,
        isUpdating = isUpdating,
        navController = navController,
        viewModel = viewModel
    ) {
        scheduleEntries?.let { scheduleEntries ->
            Crossfade(scheduleEntries.isEmpty()) { isEmpty ->
                when (isEmpty) {
                    true -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(stringResource(R.string.no_classes_message))
                        }
                    }
                    false -> {
                        ScheduleEntriesList(
                            isUpdating = isUpdating,
                            scheduleEntries = scheduleEntries,
                            timeNow = timeNow,
                            onRefresh = viewModel::updateClasses
                        )
                    }
                }
            }
        }
    }
}
