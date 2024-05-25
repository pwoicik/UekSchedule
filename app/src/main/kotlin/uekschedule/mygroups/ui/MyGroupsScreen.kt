package uekschedule.mygroups.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import uekschedule.browser.ui.BrowserScreen

@Parcelize
object MyGroupsScreen : Screen

data class MyGroupsState(
    val eventSink: (MyGroupsEvent) -> Unit,
) : CircuitUiState

sealed interface MyGroupsEvent : CircuitUiEvent {
    data object AddGroup : MyGroupsEvent
}

class MyGroupsPresenter(
    private val navigator: Navigator,
) : Presenter<MyGroupsState> {
    @Composable
    override fun present(): MyGroupsState {
        return MyGroupsState(
            eventSink = {
                when (it) {
                    MyGroupsEvent.AddGroup,
                    -> navigator.goTo(BrowserScreen)
                }
            },
        )
    }
}

@Composable
fun MyGroupsUi(
    state: MyGroupsState,
    modifier: Modifier = Modifier,
) {
    Column(modifier.statusBarsPadding()) {
        FilledTonalButton(onClick = { state.eventSink(MyGroupsEvent.AddGroup) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}
