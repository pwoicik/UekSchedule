package uekschedule.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.internal.BackHandler
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize
import uekschedule.mygroups.ui.MyGroupsScreen

@Parcelize
data object HomeScreen : Screen

data class HomeState(
    val currentTab: Tab,
    val tabs: ImmutableList<Tab>,
    val eventSink: (HomeEvent) -> Unit,
) : CircuitUiState

data class Tab(
    val label: String,
    val icon: ImageVector,
    val screen: Screen,
)

sealed interface HomeEvent : CircuitUiEvent {
    @JvmInline
    value class Navigation(val event: NavEvent) : HomeEvent

    data class TabSelected(val tab: Tab) : HomeEvent
}

class HomePresenter(
    private val navigator: Navigator,
) : Presenter<HomeState> {
    @Composable
    override fun present(): HomeState {
        val strings = LocalStrings.current
        val tabs = remember {
            persistentListOf(
                Tab(
                    label = strings.schedule,
                    icon = Icons.Default.CalendarMonth,
                    screen = MyGroupsScreen, // TODO
                ),
                Tab(
                    label = strings.myGroupsTab,
                    icon = Icons.Default.Groups,
                    screen = MyGroupsScreen,
                ),
                Tab(
                    label = strings.settings,
                    icon = Icons.Default.Settings,
                    screen = HomeScreen, // TODO
                ),
            )
        }
        var currentTab by remember { mutableStateOf(tabs.first()) }
        return HomeState(
            currentTab = currentTab,
            tabs = tabs,
            eventSink = {
                when (it) {
                    is HomeEvent.Navigation,
                    -> navigator.onNavEvent(it.event)

                    is HomeEvent.TabSelected,
                    -> currentTab = it.tab
                }
            },
        )
    }
}

@Composable
fun HomeUi(
    state: HomeState,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        val bottomBarScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
        if (state.currentTab != state.tabs.first()) {
            BackHandler {
                state.eventSink(HomeEvent.TabSelected(state.tabs.first()))
            }
        }
        // TODO
        CircuitContent(
            screen = state.currentTab.screen,
            onNavEvent = { state.eventSink(HomeEvent.Navigation(it)) },
            modifier = Modifier
                .weight(1f)
                .nestedScroll(bottomBarScrollBehavior.nestedScrollConnection),
        )
        BottomAppBar(
            scrollBehavior = bottomBarScrollBehavior,
        ) {
            state.tabs.forEach {
                NavigationBarItem(
                    selected = state.currentTab == it,
                    onClick = { state.eventSink(HomeEvent.TabSelected(it)) },
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.label,
                        )
                    },
                    label = { Text(text = it.label) },
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}
