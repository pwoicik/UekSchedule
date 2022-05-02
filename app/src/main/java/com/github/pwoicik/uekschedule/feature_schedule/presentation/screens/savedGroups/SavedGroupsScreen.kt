package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarHost
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithUndo
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.YourGroupsNavGraph
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.AllGroupsScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.GroupSubjectsScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.SingleGroupSchedulePreviewScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@YourGroupsNavGraph(start = true)
@Destination
@Composable
fun SavedGroupsScreen(
    navigator: DestinationsNavigator,
    mainNavigator: DestinationsNavigator,
    rootNavigator: DestinationsNavigator,
    viewModel: SavedGroupsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    LaunchedEffect(state.userMessage) {
        when (val userMessage = state.userMessage) {
            is UserMessage.GroupDeleted -> launch {
                val result = snackbarHostState.showSnackbar(
                    visuals = SnackbarVisualsWithUndo(
                        message = context.resources.getString(
                            R.string.group_deleted,
                            userMessage.gwc.group.name
                        ),
                        context = context
                    )
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.emit(SavedGroupsEvent.UndoGroupDeletion(userMessage.gwc))
                }
                viewModel.emit(SavedGroupsEvent.UserMessageSeen)
            }
            UserMessage.None -> snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mainNavigator.navigate(AllGroupsScreenDestination) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_group)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Crossfade(
            targetState = state.groups == null,
            modifier = Modifier.padding(innerPadding)
        ) { isFetching ->
            when (isFetching) {
                true -> {}
                false -> {
                    GroupList(
                        groups = state.groups!!,
                        onGroupClick = {
                            rootNavigator.navigate(
                                SingleGroupSchedulePreviewScreenDestination(it.id, it.name)
                            )
                        },
                        onFavoriteGroupClick = { viewModel.emit(SavedGroupsEvent.FavoriteGroup(it)) },
                        onEditGroupClick = {
                            navigator.navigate(
                                GroupSubjectsScreenDestination(it.id, it.name)
                            )
                        },
                        onDeleteGroupClick = { viewModel.emit(SavedGroupsEvent.DeleteGroup(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupList(
    groups: List<Group>,
    onGroupClick: (Group) -> Unit,
    onFavoriteGroupClick: (Group) -> Unit,
    onEditGroupClick: (Group) -> Unit,
    onDeleteGroupClick: (Group) -> Unit,
) {
    if (groups.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(stringResource(R.string.no_saved_groups))
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(24.dp)
        ) {
            items(
                items = groups,
                key = Group::id
            ) { group ->
                ListItem(
                    group = group,
                    onItemClick = { onGroupClick(group) },
                    onFavoriteItemClick = { onFavoriteGroupClick(group) },
                    onEditItemClick = { onEditGroupClick(group) },
                    onDeleteItemClick = { onDeleteGroupClick(group) }
                )
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
private fun LazyItemScope.ListItem(
    group: Group,
    onItemClick: () -> Unit,
    onFavoriteItemClick: () -> Unit,
    onEditItemClick: () -> Unit,
    onDeleteItemClick: () -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.animateItemPlacement()
    ) {
        ListItemLayout {
            Surface(
                tonalElevation = 8.dp,
                onClick = onItemClick
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = group.name,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = stringResource(R.string.preview_group)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                AnimatedContent(
                    targetState = group.isFavorite,
                    contentAlignment = Alignment.Center,
                    transitionSpec = {
                        scaleIn(
                            initialScale = 0.0f,
                            animationSpec = tween(220, delayMillis = 90)
                        ) with fadeOut(animationSpec = tween(500))
                    }
                ) { isFavorite ->
                    IconButton(onClick = onFavoriteItemClick) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = stringResource(R.string.mark_group_as_favorite),
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.alpha(0.7f)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onEditItemClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_classes),
                        modifier = Modifier
                            .size(20.dp)
                            .alpha(0.7f)
                    )
                }
                IconButton(onClick = onDeleteItemClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_group),
                        modifier = Modifier
                            .size(20.dp)
                            .alpha(0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private inline fun ListItemLayout(
    content: @Composable () -> Unit
) {
    Layout(content = content) { measurables, constraints ->
        val actionsPlaceable = measurables[1].measure(constraints)
        val titlePlaceable = measurables[0].measure(
            constraints.copy(minHeight = (actionsPlaceable.height * 1.15).roundToInt())
        )

        layout(titlePlaceable.width, titlePlaceable.height + actionsPlaceable.height) {
            titlePlaceable.placeRelative(0, 0)
            actionsPlaceable.placeRelative(0, titlePlaceable.height)
        }
    }
}
