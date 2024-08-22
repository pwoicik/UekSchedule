package com.github.pwoicik.uekschedule.presentation.navigation.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.github.pwoicik.uekschedule.features.activities.presentation.screens.createActivity.CreateActivityNavigator
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedule.ScheduleNavigator
import com.github.pwoicik.uekschedule.features.search.presentation.screens.search.SearchNavigator
import com.github.pwoicik.uekschedule.presentation.navigation.NavGraphs
import com.github.pwoicik.uekschedule.presentation.navigation.navigators.MainNavigator
import com.github.pwoicik.uekschedule.presentation.navigation.screens.destinations.YourGroupsScreenDestination
import com.github.pwoicik.uekschedule.presentation.navigation.screens.main.components.MainScreenScaffold
import com.github.pwoicik.uekschedule.presentation.navigation.screens.yourGroups.YourGroupsScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import timber.log.Timber

@Destination
@Composable
internal fun MainScreen(
    rootNavigator: DestinationsNavigator
) {
    val navController = rememberNavController()
    val currentDestination by navController.currentDestinationAsState()

    LaunchedEffect(currentDestination) {
        Timber.tag("mainScreen navGraph destination").d(currentDestination?.route.toString())
    }

    val snackbarHostState = remember { SnackbarHostState() }

    MainScreenScaffold(
        snackbarHostState = snackbarHostState,
        currentDestination = currentDestination,
        onDestinationClick = { destination ->
            navController.navigate(destination.direction) {
                launchSingleTop = true
                popUpTo(NavGraphs.main.startRoute.route)
            }
        }
    ) {
        DestinationsNavHost(
            navGraph = NavGraphs.main,
            navController = navController,
            modifier = Modifier.padding(it),
            dependenciesContainerBuilder = {
                val mainNavigator = remember(destinationsNavigator, rootNavigator) {
                    MainNavigator(destinationsNavigator, rootNavigator)
                }
                dependency(mainNavigator as SearchNavigator)
                dependency(mainNavigator as CreateActivityNavigator)
                dependency(mainNavigator as ScheduleNavigator)
            }
        ) {
            composable(YourGroupsScreenDestination) {
                YourGroupsScreen(
                    mainNavigator = destinationsNavigator,
                    rootNavigator = rootNavigator
                )
            }
        }
    }
}
