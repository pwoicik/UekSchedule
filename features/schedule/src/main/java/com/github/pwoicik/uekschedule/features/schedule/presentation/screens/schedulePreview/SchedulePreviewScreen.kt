package com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedulePreview

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.github.pwoicik.uekschedule.common.R
import com.github.pwoicik.uekschedule.features.schedule.presentation.components.ScheduleEntriesList
import com.github.pwoicik.uekschedule.features.schedule.presentation.components.firstVisibleItemIndex
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedulePreview.components.SchedulePreviewScaffold
import com.github.pwoicik.uekschedule.presentation.components.NoResults
import com.github.pwoicik.uekschedule.presentation.components.SnackbarVisualsWithError
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

interface SchedulePreviewNavigator {
    fun navigateUp()
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = SchedulePreviewNavArgs::class)
@Composable
fun SchedulePreviewScreen(
    navigator: SchedulePreviewNavigator,
) {
    val viewModel: SchedulePreviewViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()
    val timeNow by viewModel.timeFlow.collectAsState()

    val firstEntryIdx by derivedStateOf {
        state.filteredEntries.firstVisibleItemIndex(timeNow.toLocalDate())
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                SchedulePreviewViewModel.UiEvent.HideSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
                SchedulePreviewViewModel.UiEvent.ShowErrorSnackbar -> {
                    launch {
                        val result = snackbarHostState.showSnackbar(
                            visuals = SnackbarVisualsWithError(
                                message = context.resources.getString(R.string.couldnt_connect),
                                actionLabel = context.resources.getString(R.string.retry)
                            )
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.emit(SchedulePreviewEvent.RefreshButtonClicked)
                        }
                    }
                }
                SchedulePreviewViewModel.UiEvent.ScrollToToday -> {
                    lazyListState.animateScrollToItem(firstEntryIdx)
                }
            }
        }
    }

    SchedulePreviewScaffold(
        title = state.groupName,
        searchValue = state.searchValue,
        onSearchValueChange = { viewModel.emit(SchedulePreviewEvent.SearchTextChanged(it)) },
        onNavigateBack = { navigator.navigateUp() },
        isFabVisible = state.entries != null,
        onFabClick = { viewModel.emit(SchedulePreviewEvent.FabClicked) },
        isRefreshing = state.isRefreshing,
        snackbarHostState = snackbarHostState
    ) {
        Crossfade(state) { state ->
            when {
                state.didTry && state.entries == null -> {
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

                state.filteredEntries.isEmpty() -> {
                    NoResults()
                }

                state.entries?.isEmpty() == true -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(stringResource(R.string.no_classes_message2))
                    }
                }
                state.entries?.isEmpty() == false -> {
                    LaunchedEffect(firstEntryIdx) {
                        lazyListState.animateScrollToItem(firstEntryIdx)
                    }

                    ScheduleEntriesList(
                        lazyListState = lazyListState,
                        scheduleEntries = state.filteredEntries,
                        timeNow = timeNow
                    )
                }
                else -> Unit
            }
        }
    }
}
