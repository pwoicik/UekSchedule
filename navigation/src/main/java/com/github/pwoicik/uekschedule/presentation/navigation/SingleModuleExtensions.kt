package com.github.pwoicik.uekschedule.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.ramcosta.composedestinations.spec.*
import com.ramcosta.composedestinations.utils.startDestination
import com.ramcosta.composedestinations.utils.destination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class NavGraph(
    override val route: String,
    override val startRoute: Route,
    val destinations: List<DestinationSpec<*>>,
    override val nestedNavGraphs: List<NavGraph> = emptyList()
): NavGraphSpec {
    override val destinationsByRoute: Map<String, DestinationSpec<*>> = destinations.associateBy { it.route }
}

/**
 * If this [Route] is a [DestinationSpec], returns it
 *
 * If this [Route] is a [NavGraph], returns its
 * start [DestinationSpec].
 */
val Route.startAppDestination: DestinationSpec<*>
    get() = startDestination

/**
 * Finds the [DestinationSpec] correspondent to this [NavBackStackEntry].
 */
fun NavBackStackEntry.appDestination() = destination()

/**
 * Emits the currently active [DestinationSpec] whenever it changes. If
 * there is no active [DestinationSpec], no item will be emitted.
 */
val NavController.appCurrentDestinationFlow: Flow<DestinationSpec<*>>
    get() = currentBackStackEntryFlow.map { it.appDestination() }

/**
 * Gets the current [DestinationSpec] as a [State].
 */
@Composable
fun NavController.appCurrentDestinationAsState(): State<DestinationSpec<*>?> {
    return appCurrentDestinationFlow.collectAsState(initial = null)
}
