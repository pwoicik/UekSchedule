package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.CircularProgressIndicatorCentered
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups.components.AddGroupsScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups.components.AddGroupsSelectionList
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddGroupsScreen(
    navController: NavController,
    viewModel: AddGroupsViewModel = hiltViewModel()
) {
    val state by viewModel.state

    val scaffoldState = rememberScaffoldState()
    val snackbarMessage = stringResource(R.string.couldnt_connect)

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddGroupsViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(snackbarMessage)
                }
                is AddGroupsViewModel.UiEvent.HideSnackbar -> {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                }
                is AddGroupsViewModel.UiEvent.AddedGroups -> {
                    navController.navigateUp()
                }
            }
        }
    }

    AddGroupsScaffold(
        scaffoldState = scaffoldState,
        navController = navController,
        viewModel = viewModel,
        selectedGroupsActionButtonsEnabled = state.selectedGroups.isNotEmpty() &&
                !state.isSaving
    ) {
        when {
            state.availableGroupsState.isLoading -> {
                CircularProgressIndicatorCentered(modifier = Modifier.fillMaxSize())
            }
            state.availableGroupsState.isError -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(stringResource(R.string.couldnt_connect))

                    IconButton(
                        onClick = {
                            viewModel.event(AddGroupsEvent.RetryGetAvailableGroups)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.retry),
                        )
                    }
                }
            }
            else -> {
                AddGroupsSelectionList(
                    viewModel = viewModel,
                    state = state
                )
            }
        }
    }
}
