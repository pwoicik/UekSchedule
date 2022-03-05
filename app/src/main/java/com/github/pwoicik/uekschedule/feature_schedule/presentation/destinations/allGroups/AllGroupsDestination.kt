package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.allGroups

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.CircularProgressIndicator
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.Constants
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithError
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithSuccess
import com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.allGroups.components.AllGroupsColumn
import com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.allGroups.components.AllGroupsScaffold
import com.google.accompanist.insets.LocalWindowInsets
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navGraph = "mainScreen")
@Composable
fun AllGroupsDestination(
    parentNavigator: DestinationsNavigator,
    viewModel: AllGroupsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val snackbarErrorMessage = stringResource(R.string.couldnt_connect)
    val snackbarErrorActionLabel = stringResource(R.string.retry)
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                AllGroupsViewModel.UiEvent.HideSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
                AllGroupsViewModel.UiEvent.ShowErrorSnackbar -> {
                    launch {
                        val result = snackbarHostState.showSnackbar(
                            visuals = SnackbarVisualsWithError(
                                message = snackbarErrorMessage,
                                actionLabel = snackbarErrorActionLabel
                            )
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.emit(AllGroupsEvent.RetryGroupsFetch)
                        }
                    }
                }
                is AllGroupsViewModel.UiEvent.NavigateToGroupPreview -> {
                    parentNavigator.navigate(event.destination)
                }
                is AllGroupsViewModel.UiEvent.ShowSavedGroupSnackbar -> {
                    launch {
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

    val insets = LocalWindowInsets.current
    val navBarsPadding = with(LocalDensity.current) { insets.navigationBars.bottom.toDp() }
    val imePadding = with(LocalDensity.current) { insets.ime.bottom.toDp() }
    val bottomPadding = PaddingValues(
        bottom = imePadding.coerceAtLeast(
            Constants.BottomBarHeight + navBarsPadding
        )
    )

    AllGroupsScaffold(
        searchFieldValue = state.searchText,
        onSearchValueChange = { viewModel.emit(AllGroupsEvent.SearchTextChanged(it)) },
        snackbarHostState = snackbarHostState,
        snackbarPadding = bottomPadding
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = state.groups != null,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                val filteredGroups by derivedStateOf { state.filteredGroups }
                AllGroupsColumn(
                    groups = filteredGroups,
                    onGroupClick = {
                        viewModel.emit(AllGroupsEvent.GroupCardClicked(it))
                    },
                    onGroupAddButtonClick = {
                        viewModel.emit(AllGroupsEvent.GroupSaveButtonClicked(it))
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottomPadding)
                )
            }
            AnimatedVisibility(
                visible = state.isLoading,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                CircularProgressIndicator(
                    isSpinning = state.isLoading,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
