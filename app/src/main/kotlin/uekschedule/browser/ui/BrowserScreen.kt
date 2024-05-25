package uekschedule.browser.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import arrow.fx.coroutines.parMap
import cafe.adriel.lyricist.LocalStrings
import cafe.adriel.lyricist.strings
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.overlay.Overlay
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import uekschedule.browser.domain.model.Schedular
import uekschedule.browser.domain.usecase.GetSchedulars
import uekschedule.schedule.ui.ScheduleScreen
import kotlin.time.Duration.Companion.milliseconds

@Parcelize
data object BrowserScreen : Screen

sealed interface BrowserState : CircuitUiState {
    data object Loading : BrowserState

    data class Loaded(
        val isFetching: Boolean,
        val query: TextFieldState,
        val filters: ImmutableList<Filter>,
        val schedulars: ImmutableList<Schedular>,
        val eventSink: (BrowserEvent) -> Unit,
    ) : BrowserState
}

sealed interface BrowserEvent : CircuitUiEvent {
    data class FilterSelected(val filter: Filter) : BrowserEvent
    data class SchedularClicked(val schedular: Schedular) : BrowserEvent
}

data class Filter(
    val type: Schedular.Type,
    val label: String,
    val selected: Boolean,
)

class BrowserPresenter(
    private val navigator: Navigator,
    private val getSchedulars: GetSchedulars,
) : Presenter<BrowserState> {
    private var isFetching by mutableStateOf(false)

    @Composable
    override fun present(): BrowserState {
        val query = rememberTextFieldState()
        val strings = LocalStrings.current
        var filters by remember {
            mutableStateOf(
                persistentListOf(
                    Filter(
                        type = Schedular.Type.Group,
                        label = strings.groupsFilter,
                        selected = true,
                    ),
                    Filter(
                        type = Schedular.Type.Teacher,
                        label = strings.teachersFilter,
                        selected = false,
                    ),
                ),
            )
        }
        val schedulars = getSchedulars(query.text.toString(), filters)
            ?: return BrowserState.Loading
        return BrowserState.Loaded(
            isFetching = isFetching,
            query = query,
            filters = filters,
            schedulars = schedulars,
            eventSink = { event ->
                when (event) {
                    is BrowserEvent.FilterSelected,
                    -> filters = filters.mutate {
                        val i = it.indexOf(event.filter)
                        it[i] = it[i].copy(selected = !it[i].selected)
                    }

                    is BrowserEvent.SchedularClicked,
                    -> navigator.goTo(ScheduleScreen(event.schedular.id))
                }
            },
        )
    }

    @Composable
    fun getSchedulars(
        query: String,
        filters: PersistentList<Filter>,
    ): ImmutableList<Schedular>? {
        val schedulars by produceState<ImmutableList<Schedular>?>(null, filters) {
            isFetching = true
            val types = filters.filter { it.selected }.map { it.type }
            value = withContext(Dispatchers.Default) {
                types
                    .parMap { getSchedulars(it) }
                    .flatten()
                    .sortedBy { it.name.lowercase() }
                    .let(::ImmutableListAdapter)
            }
        }
        val filteredSchedulars by produceState<ImmutableList<Schedular>?>(null, schedulars, query) {
            val flow = snapshotFlow { query }
                .debounce(400.milliseconds)
            withContext(Dispatchers.Default) {
                flow.collect { query ->
                    value = schedulars?.filter {
                        it.name.contains(query, ignoreCase = true)
                    }?.let(::ImmutableListAdapter)
                    isFetching = false
                }
            }
        }
        return filteredSchedulars
    }
}

@Composable
fun BrowserUi(
    state: BrowserState,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        when (state) {
            BrowserState.Loading,
            -> Spinner(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .statusBarsPadding(),
            )

            is BrowserState.Loaded,
            -> Loaded(state)
        }
    }
}

@Composable
private fun Loaded(
    state: BrowserState.Loaded,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .statusBarsPadding()
                .padding(8.dp, 8.dp, 8.dp, 12.dp),
        ) {
            Search(state.query)
        }
        Filters(
            filters = state.filters,
            onSelect = { state.eventSink(BrowserEvent.FilterSelected(it)) },
        )
        if (state.schedulars.isEmpty()) {
            Text(
                text = strings.noResults,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 24.dp),
            )
        } else {
            var showSchedule: Schedular? by remember { mutableStateOf(null) }
            showSchedule?.let {
                ModalBottomSheet(
                    sheetState = rememberModalBottomSheetState(),
                    onDismissRequest = { showSchedule = null },
                    contentWindowInsets = { WindowInsets(0) },
                    dragHandle = {
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                        ) {
                            Box(
                                Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(vertical = 12.dp)
                                    .size(50.dp, 5.dp)
                                    .heightIn(
                                        min = WindowInsets.statusBars.asPaddingValues()
                                            .calculateTopPadding(),
                                    )
                                    .background(
                                        MaterialTheme.colorScheme.onSecondaryContainer,
                                        CircleShape
                                    ),
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = it.name,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp),
                                )
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = Icons.Default.SaveAlt,
                                        contentDescription = null,
                                    )
                                }
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = Icons.Default.OpenInFull,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                            }
                        }
                    },
                ) {
                    CircuitContent(
                        screen = ScheduleScreen(it.id),
                    )
                }
            }
            Box {
                Schedulars(
                    schedulars = state.schedulars,
                    onClick = {
                        showSchedule = it
//                        state.eventSink(BrowserEvent.SchedularClicked(it))
                    },
                )
                androidx.compose.animation.AnimatedVisibility(
                    visible = state.isFetching,
                    enter = slideInVertically { -it } + scaleIn() + fadeIn(),
                    exit = slideOutVertically { -it } + scaleOut() + fadeOut(),
                    modifier = Modifier.align(Alignment.TopCenter),
                ) {
                    Spinner(
                        Modifier
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .padding(8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun Search(
    state: TextFieldState,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    BasicTextField(
        state = state,
        lineLimits = TextFieldLineLimits.SingleLine,
        textStyle = LocalTextStyle.current.copy(MaterialTheme.colorScheme.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        decorator = {
            Row {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(8.dp))
                Box {
                    if (state.text.isEmpty()) {
                        Text(
                            text = "Search",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    it()
                }
            }
        },
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun Filters(
    filters: ImmutableList<Filter>,
    onSelect: (Filter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .padding(horizontal = 8.dp)
            .selectableGroup(),
    ) {
        filters.forEach {
            ElevatedFilterChip(
                selected = it.selected,
                onClick = { onSelect(it) },
                label = { Text(text = it.label) },
                leadingIcon = if (it.selected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                        )
                    }
                } else {
                    null
                },
            )
        }
    }
}

@Composable
private fun Schedulars(
    schedulars: ImmutableList<Schedular>,
    onClick: (Schedular) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = WindowInsets.ime.asPaddingValues(),
        modifier = modifier,
    ) {
        val letter = schedulars.first().name.first()
        stickyHeader(letter) {
            LetterHeader(letter)
        }
        schedulars.windowed(size = 2, partialWindows = true) {
            val first = it.first()
            item(first.id) {
                Group(
                    schedular = first,
                    onClick = { onClick(first) },
                    modifier = Modifier.animateItem(),
                )
            }
            val second = it.getOrNull(1) ?: return@windowed
            val firstLetter = second.name.first()
            if (firstLetter.equals(first.name.first(), ignoreCase = true)) return@windowed
            item {
                Spacer(Modifier.height(18.dp))
            }
            stickyHeader(firstLetter) {
                LetterHeader(firstLetter)
            }
        }
    }
}

@Composable
private fun LetterHeader(
    letter: Char,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp, 2.dp, 12.dp, 2.dp),
    ) {
        Text(
            text = letter.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
        )
        Spacer(Modifier.width(16.dp))
        val lineColor = MaterialTheme.colorScheme.onSurface.copy(0.3f)
        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(Dp.Hairline),
        ) {
            drawLine(
                color = lineColor,
                start = Offset.Zero,
                end = Offset(size.width, 0f),
            )
        }
    }
}

@Composable
private fun Group(
    schedular: Schedular,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(16.dp, 12.dp),
    ) {
        Text(
            text = schedular.name,
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun Spinner(modifier: Modifier = Modifier) {
    val color1 = MaterialTheme.colorScheme.onSurface
    val color2 = MaterialTheme.colorScheme.tertiary
    val transition = rememberInfiniteTransition(label = "spinnin'")
    val startAngle1 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1_400, easing = LinearEasing),
        ),
        label = "startAngle1",
    )
    val startAngle2 by transition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1_200, easing = LinearEasing),
        ),
        label = "startAngle2",
    )
    Canvas(modifier = modifier.size(48.dp)) {
        val width = 5.dp.toPx()
        val offset1 = width / 2
        val size1 = size.width - width
        drawArc(
            color = color1,
            startAngle = startAngle1,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(width),
            topLeft = Offset(offset1, offset1),
            size = Size(size1, size1),
        )

        val gap = 4.dp.toPx()
        val offset2 = offset1 + width + gap
        val size2 = size1 - offset2 - width / 2 - gap
        drawArc(
            color = color2,
            startAngle = startAngle2,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(width),
            topLeft = Offset(offset2, offset2),
            size = Size(size2, size2),
        )
    }
}

private class ScheduleOverlay(
    private val schedular: Schedular,
) : Overlay<Unit> {
    @Composable
    override fun Content(navigator: OverlayNavigator<Unit>) {
        CircuitContent(ScheduleScreen(schedular.id))
    }
}
