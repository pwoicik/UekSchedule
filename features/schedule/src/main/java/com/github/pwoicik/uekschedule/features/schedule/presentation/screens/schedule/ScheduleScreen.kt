package com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.common.Constants
import com.github.pwoicik.uekschedule.features.schedule.presentation.components.ScheduleEntriesList
import com.github.pwoicik.uekschedule.features.schedule.presentation.components.firstVisibleItemIndex
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedule.components.ScheduleScaffold
import com.github.pwoicik.uekschedule.presentation.components.NoResults
import com.github.pwoicik.uekschedule.presentation.components.SnackbarVisualsWithError
import com.github.pwoicik.uekschedule.presentation.util.openInBrowser
import com.github.pwoicik.uekschedule.resources.R
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

interface ScheduleNavigator {
    fun openPreferences()
}

@Destination
@Composable
fun ScheduleScreen(
    navigator: ScheduleNavigator
) {
    val viewModel: ScheduleViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()
    val timeNow by viewModel.timeFlow.collectAsState()

    val firstEntryIdx by remember {
        derivedStateOf {
            state.filteredEntries.firstVisibleItemIndex(timeNow.toLocalDate())
        }
    }
    val listState = rememberLazyListState()
    LaunchedEffect(firstEntryIdx) {
        if (firstEntryIdx == 0) return@LaunchedEffect
        listState.animateScrollToItem(firstEntryIdx)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                ScheduleViewModel.UiEvent.ScrollToToday -> {
                    listState.animateScrollToItem(firstEntryIdx)
                }

                ScheduleViewModel.UiEvent.ShowErrorSnackbar -> {
                    launch {
                        val result = snackbarHostState.showSnackbar(
                            visuals = SnackbarVisualsWithError(
                                message = context.resources.getString(R.string.couldnt_connect),
                                actionLabel = context.resources.getString(R.string.retry)
                            )
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.emit(ScheduleEvent.RefreshButtonClicked)
                        }
                    }
                }

                ScheduleViewModel.UiEvent.HideSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
        }
    }

    val hasEntries = state.entries?.isEmpty() == false
    ScheduleScaffold(
        isSearchButtonEnabled = hasEntries,
        searchValue = state.searchValue,
        onSearchValueChange = { viewModel.emit(ScheduleEvent.SearchTextChanged(it)) },
        isFabVisible = hasEntries,
        onFabClick = { viewModel.emit(ScheduleEvent.FabClicked) },
        isRefreshing = state.isRefreshing,
        onRefreshButtonClick = { viewModel.emit(ScheduleEvent.RefreshButtonClicked) },
        onMoodleButtonClick = { context.openInBrowser(Constants.MOODLE_URL) },
        onPreferencesButtonClick = { navigator.openPreferences() },
        snackbarHostState = snackbarHostState
    ) {
        Crossfade(
            targetState = state.dataState,
            label = "data state crossfade"
        ) { dataState ->
            when (dataState) {
                DataState.LOADING -> Unit
                DataState.SUCCESS -> {
                    ScheduleEntriesList(
                        lazyListState = listState,
                        scheduleEntries = state.filteredEntries,
                        timeNow = timeNow
                    )
                }

                DataState.NO_CONNECTION -> {
                    AnimatedVisibility(
                        visible = !state.isRefreshing,
                        enter = fadeIn(tween(delayMillis = 500)),
                        exit = fadeOut()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = stringResource(R.string.couldnt_connect),
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }

                DataState.NO_RESULTS -> {
                    NoResults()
                }

                DataState.NO_GROUPS -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(stringResource(R.string.no_saved_groups))
                    }

                }

                DataState.NO_CLASSES -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(stringResource(R.string.no_classes_message))
                    }
                }
            }
        }
    }
}
