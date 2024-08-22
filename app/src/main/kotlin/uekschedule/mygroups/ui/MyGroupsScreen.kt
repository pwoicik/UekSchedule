package uekschedule.mygroups.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import kotlinx.collections.immutable.adapters.ImmutableMapAdapter
import kotlinx.parcelize.Parcelize
import uekschedule.browser.domain.model.Schedulable
import uekschedule.browser.ui.BrowserScreen
import uekschedule.mygroups.domain.usecase.GetSchedulables

@Parcelize
object MyGroupsScreen : Screen

sealed interface MyGroupsState : CircuitUiState

data object LoadingState : MyGroupsState

private data class LoadedState(
    val schedulables: ImmutableMap<String, ImmutableList<Schedulable>>,
    val eventSink: (Event) -> Unit,
) : MyGroupsState {
    sealed interface Event : CircuitUiEvent {
        data object AddGroup : Event
    }
}

class MyGroupsPresenter(
    private val navigator: Navigator,
    private val getSchedulables: GetSchedulables,
) : Presenter<MyGroupsState> {
    @Composable
    override fun present(): MyGroupsState {
        val schedulables = getSchedulables() ?: return LoadingState
        return LoadedState(
            schedulables = schedulables,
            eventSink = {
                when (it) {
                    LoadedState.Event.AddGroup,
                    -> navigator.goTo(BrowserScreen)
                }
            },
        )
    }

    @Composable
    private fun getSchedulables(): ImmutableMap<String, ImmutableList<Schedulable>>? {
        val strings = strings
        val schedulables by produceRetainedState<
                ImmutableMap<String, ImmutableList<Schedulable>>?
                >(null) {
            getSchedulables.invoke().collect { s ->
                value = s
                    .groupBy(
                        keySelector = {
                            when (it.id) {
                                is Schedulable.Id.Group -> strings.groups
                                is Schedulable.Id.Teacher -> strings.teachers
                            }
                        }
                    )
                    .mapValues { ImmutableListAdapter(it.value) }
                    .let(::ImmutableMapAdapter)
            }
        }
        return schedulables
    }
}

@Composable
fun MyGroupsUi(
    state: MyGroupsState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
    ) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        TopAppBar(
            title = { Text(strings.myGroupsTab) },
            scrollBehavior = scrollBehavior,
        )
        when (state) {
            LoadingState -> Unit
            is LoadedState -> Loaded(
                state = state,
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            )
        }
    }
}

@Composable
private fun Loaded(
    state: LoadedState,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        LazyColumn {
            item {
                AddGroupButton(
                    onClick = { state.eventSink(LoadedState.Event.AddGroup) },
                    modifier = Modifier.padding(24.dp, 0.dp, 24.dp, 16.dp),
                )
            }
            state.schedulables.forEach { (k, v) ->
                if (state.schedulables.size > 1) {
                    stickyHeader {
                        Text(
                            text = k,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(12.dp, 8.dp, 12.dp, 0.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceContainer,
                                    shape = CircleShape,
                                )
                                .padding(8.dp, 4.dp),
                        )
                    }
                }
                items(v) {
                    Group(
                        Schedulable = it,
                        modifier = Modifier.padding(24.dp, 16.dp, 24.dp, 0.dp),
                    )
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun AddGroupButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = MaterialTheme.colorScheme.surfaceVariant
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .drawBehind {
                val strokeWidth = 2.5.dp.toPx()
                drawRoundRect(
                    color = borderColor,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.chainPathEffect(
                            inner = PathEffect.cornerPathEffect(8.dp.toPx()),
                            outer = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(10.dp.toPx(), 15.dp.toPx()),
                                phase = 15.dp.toPx(),
                            ),
                        ),
                    ),
                )
                drawRoundRect(
                    color = borderColor,
                    alpha = 0.2f,
                    topLeft = Offset(strokeWidth * 0.5f, strokeWidth * 0.5f),
                    size = Size(
                        size.width - strokeWidth,
                        size.height - strokeWidth,
                    ),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                )
            }
            .padding(16.dp),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = strings.addGroup,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun Group(
    Schedulable: Schedulable,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.shapes.medium),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable(role = Role.Button) { /* TODO */ }
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                .padding(8.dp, 16.dp),
        ) {
            Text(
                text = Schedulable.name,
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Row {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.TwoTone.Favorite,
                    contentDescription = null,
                    tint = Color(0xffdf2a43),
                )
            }
            Spacer(Modifier.weight(1f))
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                IconButton(onClick = { /* TODO */ }) {
                    Icon(imageVector = Icons.TwoTone.Edit, contentDescription = null)
                }
                IconButton(onClick = { /* TODO */ }) {
                    Icon(imageVector = Icons.TwoTone.Delete, contentDescription = null)
                }
            }
        }
    }
}
