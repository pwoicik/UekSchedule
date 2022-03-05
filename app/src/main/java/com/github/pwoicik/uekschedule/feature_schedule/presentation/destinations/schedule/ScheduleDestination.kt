package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.schedule

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.Constants
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithError
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList.ScheduleEntriesList
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList.firstVisibleItemIndex
import com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.PreferencesScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.schedule.components.ScheduleDestinationScaffold
import com.google.accompanist.insets.LocalWindowInsets
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination(
    navGraph = "mainScreen",
    start = true
)
@Composable
fun ScheduleDestination(
    parentNavigator: DestinationsNavigator,
    viewModel: ScheduleDestinationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val timeNow by viewModel.timeFlow.collectAsState()

    val filteredEntries by derivedStateOf {
        state.filteredEntries
    }
    val firstEntryIdx by derivedStateOf {
        filteredEntries?.firstVisibleItemIndex(timeNow.toLocalDate()) ?: 0
    }

    val listState = rememberLazyListState()
    LaunchedEffect(firstEntryIdx) {
        listState.animateScrollToItem(firstEntryIdx)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                ScheduleDestinationViewModel.UiEvent.ScrollToToday -> {
                    listState.animateScrollToItem(firstEntryIdx)
                }
                ScheduleDestinationViewModel.UiEvent.ShowErrorSnackbar -> {
                    launch {
                        val result = snackbarHostState.showSnackbar(
                            visuals = SnackbarVisualsWithError(
                                message = context.resources.getString(R.string.couldnt_connect),
                                actionLabel = context.resources.getString(R.string.retry)
                            )
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.emit(ScheduleDestinationEvent.RefreshButtonClicked)
                        }
                    }
                }
            }
        }
    }

    val insets = LocalWindowInsets.current
    val bottomPadding = with(LocalDensity.current) {
        insets.ime.bottom.toDp().coerceAtLeast(
            insets.navigationBars.bottom.toDp() + Constants.BottomBarHeight
        )
    }
    ScheduleDestinationScaffold(
        searchText = state.searchText,
        onSearchTextChange = { viewModel.emit(ScheduleDestinationEvent.SearchTextChanged(it)) },
        isFabVisible = state.entries != null,
        onFabClick = { viewModel.emit(ScheduleDestinationEvent.FabClicked) },
        fabPadding = PaddingValues(bottom = bottomPadding),
        isRefreshing = state.isRefreshing,
        onRefreshButtonClick = { viewModel.emit(ScheduleDestinationEvent.RefreshButtonClicked) },
        onPreferencesButtonClick = {
            parentNavigator.navigate(PreferencesScreenDestination)
        },
        snackbarHostState = snackbarHostState,
        snackbarPadding = PaddingValues(bottom = bottomPadding)
    ) {
        Crossfade(state.entries) { entries ->
            when {
                entries == null -> {}
                entries.isEmpty() -> {
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
                else -> {
                    ScheduleEntriesList(
                        lazyListState = listState,
                        scheduleEntries = filteredEntries!!,
                        timeNow = timeNow,
                        modifier = Modifier.padding(bottom = bottomPadding)
                    )
                }
            }
        }
    }
}
