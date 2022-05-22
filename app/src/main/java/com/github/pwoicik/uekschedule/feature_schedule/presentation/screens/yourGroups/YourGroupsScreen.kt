package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.yourGroups

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.MainNavGraph
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.NavGraphs
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.appDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.OtherActivitiesScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.SavedGroupsScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.otherActivities.OtherActivitiesScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups.SavedGroupsScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@MainNavGraph
@Destination
@Composable
fun YourGroupsScreen(
    navigator: DestinationsNavigator,
    rootNavigator: DestinationsNavigator
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    SideEffect {
        Timber.tag("mainScreen navGraph destination")
            .d(currentBackStackEntry?.appDestination()?.route)
    }

    Scaffold(
        topBar = {
            YourGroupsTopBar(
                currentNavBackStackEntry = currentBackStackEntry,
                onChangeDestination = {
                    navController.navigate(it.direction) {
                        launchSingleTop = true
                        popUpTo(NavGraphs.yourGroups.startRoute.route)
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        DestinationsNavHost(
            navGraph = NavGraphs.yourGroups,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(SavedGroupsScreenDestination) {
                SavedGroupsScreen(
                    navigator = destinationsNavigator,
                    mainNavigator = navigator,
                    rootNavigator = rootNavigator
                )
            }
            composable(OtherActivitiesScreenDestination) {
                OtherActivitiesScreen(mainNavigator = navigator)
            }
        }
    }
}
