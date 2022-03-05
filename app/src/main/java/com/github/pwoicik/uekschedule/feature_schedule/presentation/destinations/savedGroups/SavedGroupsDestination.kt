package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.savedGroups

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.Constants
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SimpleList
import com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.AllGroupsDestinationDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.CreateActivityDestinationDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.savedGroups.components.SavedGroupsScaffold
import com.google.accompanist.insets.LocalWindowInsets
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navGraph = "mainScreen")
@Composable
fun SavedGroupsDestination(
    navigator: DestinationsNavigator,
    viewModel: SavedGroupsViewModel = hiltViewModel()
) {
    val savedGroups by viewModel.savedGroups.collectAsState()
    val savedActivities by viewModel.savedActivities.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val snackbarActionLabel = stringResource(R.string.undo)
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                SavedGroupsViewModel.UiEvent.HideSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
                is SavedGroupsViewModel.UiEvent.ShowActivityDeletedSnackbar -> {
                    launch {
                        val result = snackbarHostState.showSnackbar(
                            message = context.resources.getString(
                                R.string.activity_deleted,
                                event.activity.name
                            ),
                            actionLabel = snackbarActionLabel,
                            duration = SnackbarDuration.Long
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.emit(SavedGroupsEvent.UndoActivityDeletion(event.activity))
                        }
                    }
                }
                is SavedGroupsViewModel.UiEvent.ShowGroupDeletedSnackbar -> {
                    launch {
                        val result = snackbarHostState.showSnackbar(
                            message = context.resources.getString(
                                R.string.group_deleted,
                                event.group.name
                            ),
                            actionLabel = snackbarActionLabel,
                            duration = SnackbarDuration.Long
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.emit(SavedGroupsEvent.UndoGroupDeletion(event.group))
                        }
                    }
                }
            }
        }
    }

    var currentScreen by rememberSaveable { mutableStateOf(0) }

    val insets = LocalWindowInsets.current
    val bottomPadding = with(LocalDensity.current) {
        insets.navigationBars.bottom.toDp() + Constants.BottomBarHeight
    }

    SavedGroupsScaffold(
        currentScreen = currentScreen,
        onScreenChange = { currentScreen = it },
        onAddItem = {
            navigator.navigate(
                if (currentScreen == 0)
                    AllGroupsDestinationDestination
                else CreateActivityDestinationDestination()
            )
        },
        snackbarHostState = snackbarHostState,
        contentPadding = PaddingValues(bottom = bottomPadding)
    ) {
        when (currentScreen) {
            0 -> {
                SimpleList(
                    items = savedGroups,
                    emptyListMessage = stringResource(R.string.no_saved_groups),
                    itemTitle = { Text(it.name) },
                    itemActions = {
                        IconButton(
                            onClick = {
                                viewModel.emit(SavedGroupsEvent.DeleteGroupButtonClicked(it))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(R.string.delete_group)
                            )
                        }
                    },
                    onItemClick = {}
                )
            }
            1 -> {
                SimpleList(
                    items = savedActivities,
                    emptyListMessage = stringResource(R.string.no_activities),
                    itemTitle = { Text(it.name) },
                    itemActions = {
                        IconButton(
                            onClick = {
                                viewModel.emit(
                                    SavedGroupsEvent.DeleteActivityButtonClicked(it)
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(R.string.delete_group)
                            )
                        }
                    },
                    onItemClick = { navigator.navigate(CreateActivityDestinationDestination(it.id)) }
                )
            }
            else -> throw(IllegalStateException("No such screen ($currentScreen)!"))
        }
    }
}
