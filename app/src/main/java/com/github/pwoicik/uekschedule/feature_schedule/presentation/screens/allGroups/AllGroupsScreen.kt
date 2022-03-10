package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.allGroups

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.CircularProgressIndicator
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithError
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithLoading
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithSuccess
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.allGroups.components.AllGroupsColumn
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.allGroups.components.AllGroupsScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.SingleGroupSchedulePreviewScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navGraph = "mainScreen")
@Composable
fun AllGroupsScreen(
    parentNavigator: DestinationsNavigator,
    viewModel: AllGroupsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                AllGroupsViewModel.UiEvent.HideSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
                is AllGroupsViewModel.UiEvent.ShowErrorSnackbar -> {
                    launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        val result = snackbarHostState.showSnackbar(
                            visuals = SnackbarVisualsWithError(
                                message = context.resources.getString(R.string.couldnt_connect),
                                actionLabel = context.resources.getString(R.string.retry)
                            )
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.emit(event.eventToRepeat)
                        }
                    }
                }
                is AllGroupsViewModel.UiEvent.ShowSavingGroupSnackbar -> {
                    launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            visuals = SnackbarVisualsWithLoading(
                                message = context.resources.getString(
                                    R.string.saving_group,
                                    event.group.name
                                )
                            )
                        )
                    }
                }
                is AllGroupsViewModel.UiEvent.ShowSavedGroupSnackbar -> {
                    launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            visuals = SnackbarVisualsWithSuccess(
                                message = context.resources.getString(
                                    R.string.group_saved,
                                    event.group.name
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    AllGroupsScaffold(
        searchFieldValue = state.searchValue,
        onSearchValueChange = { viewModel.emit(AllGroupsEvent.SearchTextChanged(it)) },
        isSearchFieldFocused = state.groups != null,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Crossfade(targetState = state) { state ->
                when {
                    state.didTry && state.groups == null -> {
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
                    state.groups != null -> {
                        val filteredGroups by derivedStateOf { state.filteredGroups }
                        AllGroupsColumn(
                            groups = filteredGroups,
                            onGroupClick = {
                                parentNavigator.navigate(SingleGroupSchedulePreviewScreenDestination(it.id, it.name))
                            },
                            areGroupAddButtonsEnabled = !state.isSaving,
                            onGroupAddButtonClick = {
                                viewModel.emit(AllGroupsEvent.GroupSaveButtonClicked(it))
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> { /*DISPLAY NOTHING BEFORE FIRST SYNC*/ }
                }
            }
            AnimatedVisibility(
                visible = state.isLoading,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                CircularProgressIndicator(
                    isSpinning = state.isLoading,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
    }
}
