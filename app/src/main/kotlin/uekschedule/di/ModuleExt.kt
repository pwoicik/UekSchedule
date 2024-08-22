package uekschedule.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import org.koin.core.module.Module
import org.koin.core.qualifier.qualifier
import org.koin.core.scope.Scope

inline fun <reified Sc : Screen> Module.presenterFactory(
    crossinline presenter: Scope.(screen: Sc, navigator: Navigator) -> Presenter<*>,
) = factory(qualifier<Sc>()) {
    Presenter.Factory { screen, navigator, _ ->
        when (screen) {
            is Sc -> presenter(screen, navigator)
            else -> null
        }
    }
}

inline fun <reified Sc : Screen, St : CircuitUiState> Module.uiFactory(
    crossinline ui: @Composable Scope.(state: St, modifier: Modifier) -> Unit,
) = factory(qualifier<Sc>()) {
    Ui.Factory { screen, _ ->
        when (screen) {
            is Sc -> ui<St> { state, modifier -> ui(state, modifier) }
            else -> null
        }
    }
}
