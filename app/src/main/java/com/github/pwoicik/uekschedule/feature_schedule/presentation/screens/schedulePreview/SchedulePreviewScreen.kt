package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedulePreview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithError
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList.ScheduleEntriesList
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList.firstVisibleItemIndex
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedulePreview.components.SchedulePreviewScaffold
import com.google.accompanist.insets.navigationBarsPadding
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = SchedulePreviewNavArgs::class)
@Composable
fun SchedulePreviewScreen(
    navigator: DestinationsNavigator,
    viewModel: SchedulePreviewViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val timeNow by viewModel.timeFlow.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

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
                            viewModel.refreshData()
                        }
                    }
                }
            }
        }
    }

    SchedulePreviewScaffold(
        title = state.groupName,
        onNavigateBack = { navigator.navigateUp() },
        snackbarHostState = snackbarHostState,
        isRefreshing = state.isRefreshing
    ) {
        Crossfade(targetState = state.entries) { entries ->
            when {
                entries == null -> {
                    AnimatedVisibility(
                        visible = !state.isRefreshing,
                        enter = fadeIn(tween(delayMillis = 500)),
                        exit = fadeOut(),
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
                entries.isEmpty() -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(stringResource(R.string.no_classes_message2))
                    }
                }
                else -> {
                    @Suppress("NAME_SHADOWING")
                    val scheduleEntries by derivedStateOf {
                        entries.groupBy(ScheduleEntry::startDate)
                    }
                    val firstEntryIdx by derivedStateOf {
                        scheduleEntries.firstVisibleItemIndex(timeNow.toLocalDate())
                    }

                    val lazyListState = rememberLazyListState()
                    LaunchedEffect(firstEntryIdx) {
                        lazyListState.animateScrollToItem(firstEntryIdx)
                    }

                    ScheduleEntriesList(
                        lazyListState = lazyListState,
                        scheduleEntries = scheduleEntries,
                        timeNow = timeNow,
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            }
        }
    }
}
