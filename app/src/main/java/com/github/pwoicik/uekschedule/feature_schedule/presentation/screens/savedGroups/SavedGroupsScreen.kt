package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SimpleList
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.AllGroupsScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.CreateActivityScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.SingleGroupSchedulePreviewScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups.components.SavedGroupsScaffold
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Destination(navGraph = "mainScreen")
@Composable
fun SavedGroupsScreen(
    parentNavigator: DestinationsNavigator,
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
                                event.gwc.group.name
                            ),
                            actionLabel = snackbarActionLabel,
                            duration = SnackbarDuration.Long
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.emit(SavedGroupsEvent.UndoGroupDeletion(event.gwc))
                        }
                    }
                }
            }
        }
    }

    var currentScreen by rememberSaveable { mutableStateOf(SavedGroupsSection.SavedGroups) }
    SavedGroupsScaffold(
        currentScreen = currentScreen,
        onScreenChange = { currentScreen = it },
        onAddItem = {
            navigator.navigate(
                when (currentScreen) {
                    SavedGroupsSection.SavedGroups -> AllGroupsScreenDestination
                    SavedGroupsSection.OtherActivities -> CreateActivityScreenDestination()
                }
            )
        },
        snackbarHostState = snackbarHostState
    ) {
        when (currentScreen) {
            SavedGroupsSection.SavedGroups -> {
                SavedGroupsScreen(
                    items = savedGroups,
                    onClickLabel = stringResource(R.string.preview_group),
                    itemTitle = {
                        Text(
                            text = it.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = true)
                        )
                    },
                    emptyListMessage = stringResource(R.string.no_saved_groups),
                    onItemClick = {
                        parentNavigator.navigate(
                            SingleGroupSchedulePreviewScreenDestination(it.id, it.name)
                        )
                    },
                    itemActions = {
                        IconButton(
                            onClick = { viewModel.emit(SavedGroupsEvent.DeleteGroupButtonClicked(it)) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(R.string.delete_group),
                                modifier = Modifier
                                    .size(22.dp)
                                    .alpha(0.7f)
                            )
                        }
                        Surface(
                            shadowElevation = 8.dp,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                        ) {
                            Box(modifier = Modifier.padding(4.dp)) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForwardIos,
                                    contentDescription = stringResource(R.string.preview_group)
                                )
                            }
                        }
                    }
                )
            }
            SavedGroupsSection.OtherActivities -> {
                SavedGroupsScreen(
                    items = savedActivities,
                    onClickLabel = stringResource(R.string.edit_activity),
                    itemTitle = { activity ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f, fill = true)
                        ) {
                            Text(
                                text = activity.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            val time = activity.startDateTime.format(timeFormatter)
                            val date = if (activity.isOneshot) {
                                activity.startDateTime.format(dateFormatter)
                            } else {
                                activity.repeatOnDaysOfWeek!!
                                    .sortedBy(DayOfWeek::ordinal)
                                    .joinToString(separator = ", ") {
                                        it.getDisplayName(
                                            TextStyle.SHORT_STANDALONE,
                                            Locale.getDefault()
                                        )
                                    }
                            }
                            Text(
                                text = "$time ($date)",
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    emptyListMessage = stringResource(R.string.no_activities),
                    onItemClick = { navigator.navigate(CreateActivityScreenDestination(it.id)) },
                    itemActions = {
                        IconButton(
                            onClick = {
                                viewModel.emit(SavedGroupsEvent.DeleteActivityButtonClicked(it))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(R.string.delete_group),
                                modifier = Modifier
                                    .size(22.dp)
                                    .alpha(0.7f)
                            )
                        }
                        Box {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit_activity),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun <T> SavedGroupsScreen(
    items: List<T>?,
    itemTitle: @Composable RowScope.(T) -> Unit,
    itemActions: @Composable RowScope.(T) -> Unit,
    onItemClick: (T) -> Unit,
    emptyListMessage: String,
    onClickLabel: String? = null
) {
    when (items) {
        null -> { /*DISPLAY NOTHING BEFORE SYNC WITH ROOM*/
        }
        else -> {
            SimpleList(
                items = items,
                itemTitle = itemTitle,
                emptyListMessage = emptyListMessage,
                itemActions = itemActions,
                onItemClick = onItemClick,
                onClickLabel = onClickLabel
            )
        }
    }
}

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
