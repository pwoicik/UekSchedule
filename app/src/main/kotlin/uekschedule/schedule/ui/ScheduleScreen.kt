package uekschedule.schedule.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize
import uekschedule.browser.domain.model.Schedular
import uekschedule.schedule.domain.model.Class
import uekschedule.schedule.domain.usecase.GetSchedule

@Parcelize
data class ScheduleScreen(
    val id: Schedular.Id,
) : Screen

sealed interface ScheduleState : CircuitUiState {
    data object Loading : ScheduleState

    data class Loaded(
        val classes: ImmutableList<Class>,
        val eventSink: (ScheduleEvent) -> Unit,
    ) : ScheduleState
}

sealed interface ScheduleEvent : CircuitUiEvent {

}

class SchedulePresenter(
    private val screen: ScheduleScreen,
    private val navigator: Navigator,
    private val getSchedule: GetSchedule,
) : Presenter<ScheduleState> {
    @Composable
    override fun present(): ScheduleState {
        val classes = getClasses() ?: return ScheduleState.Loading
        return ScheduleState.Loaded(
            classes = classes,
            eventSink = {
            },
        )
    }

    @Composable
    private fun getClasses(): ImmutableList<Class>? {
        val classes by produceState<ImmutableList<Class>?>(null, screen.id) {
            value = getSchedule(screen.id)
        }
        return classes
    }
}

@Composable
fun ScheduleUi(
    state: ScheduleState,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        when (state) {
            is ScheduleState.Loading -> Unit
            is ScheduleState.Loaded -> Classes(state.classes)
        }
    }
}

@Composable
private fun Classes(
    classes: ImmutableList<Class>,
    modifier: Modifier = Modifier,
) {
    if (classes.isEmpty()) {
        Text("No classes")
        return
    }
    LazyColumn(
        contentPadding = WindowInsets.safeDrawing
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
            .asPaddingValues(),
        modifier = modifier,
    ) {
        items(classes) {
            Text(it.name)
        }
    }
}
