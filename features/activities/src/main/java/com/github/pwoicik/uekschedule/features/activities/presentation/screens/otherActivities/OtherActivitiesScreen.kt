package com.github.pwoicik.uekschedule.features.activities.presentation.screens.otherActivities

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.domain.model.Activity
import com.github.pwoicik.uekschedule.presentation.components.SnackbarVisualsWithUndo
import com.github.pwoicik.uekschedule.resources.R
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

interface OtherActivitiesNavigator {
    fun openCreateActivity(activityId: Long = 0)
}

@Destination
@Composable
fun OtherActivitiesScreen(
    navigator: OtherActivitiesNavigator
) {
    val viewModel: OtherActivitiesViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    LaunchedEffect(state.userMessage) {
        when (val userMessage = state.userMessage) {
            is UserMessage.ActivityDeleted -> launch {
                val result = snackbarHostState.showSnackbar(
                    visuals = SnackbarVisualsWithUndo(
                        message = context.resources.getString(
                            R.string.activity_deleted,
                            userMessage.activity.name
                        ),
                        context = context
                    )
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.emit(OtherActivitiesEvent.UndoActivityDeletion(userMessage.activity))
                }
                viewModel.emit(OtherActivitiesEvent.UserMessageSeen)
            }

            UserMessage.None -> {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navigator.openCreateActivity() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.create_new_activity)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Crossfade(
            targetState = state.activities == null,
            label = "fetching crossfade",
            modifier = Modifier.padding(innerPadding)
        ) { isFetching ->
            when (isFetching) {
                true -> {}
                false -> {
                    ActivityList(
                        activities = state.activities!!,
                        onActivityClick = { navigator.openCreateActivity(it.id) },
                        onActivityDelete = { viewModel.emit(OtherActivitiesEvent.DeleteActivity(it)) }
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityList(
    activities: List<Activity>,
    onActivityClick: (Activity) -> Unit,
    onActivityDelete: (Activity) -> Unit
) {
    if (activities.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(stringResource(R.string.no_activities))
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(24.dp)
        ) {
            items(activities) { activity ->
                Surface(
                    tonalElevation = 4.dp,
                    shape = RoundedCornerShape(6.dp),
                    onClick = { onActivityClick(activity) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                                .padding(vertical = 8.dp)
                        ) {
                            Text(activity.name)
                            val time by remember {
                                derivedStateOf { activity.startDateTime.format(timeFormatter) }
                            }
                            val date by remember {
                                derivedStateOf {
                                    if (activity.isOneshot) {
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
                                }
                            }
                            Text(
                                text = "$time ($date)",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(onClick = { onActivityDelete(activity) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete_activity),
                                modifier = Modifier
                                    .size(20.dp)
                                    .alpha(0.8f)
                            )
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(LocalViewConfiguration.current.minimumTouchTargetSize)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit_activity)
                            )
                        }
                    }
                }
            }
        }
    }
}

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
