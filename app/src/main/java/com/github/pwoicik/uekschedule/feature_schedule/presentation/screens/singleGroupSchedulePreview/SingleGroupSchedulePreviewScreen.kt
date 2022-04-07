package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.singleGroupSchedulePreview

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithError
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList.ScheduleEntriesList
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList.filterEntries
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList.firstVisibleItemIndex
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.singleGroupSchedulePreview.components.SchedulePreviewScaffold
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = SchedulePreviewNavArgs::class)
@Composable
fun SingleGroupSchedulePreviewScreen(
    navigator: DestinationsNavigator,
    viewModel: SchedulePreviewViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val timeNow by viewModel.timeFlow.collectAsState()

    val filteredEntries by derivedStateOf {
        state.entries.filterEntries(state.searchValue.text)
    }
    val firstEntryIdx by derivedStateOf {
        filteredEntries?.firstVisibleItemIndex(timeNow.toLocalDate()) ?: 0
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
                        scheduleEntries = filteredEntries!!,
                        timeNow = timeNow
                    )
                }
                else -> { /*DISPLAY NOTHING BEFORE FIRST SYNC*/ }
            }
        }
    }
}
