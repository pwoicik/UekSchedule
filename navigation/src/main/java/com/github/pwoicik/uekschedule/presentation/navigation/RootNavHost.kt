package com.github.pwoicik.uekschedule.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.github.pwoicik.uekschedule.features.preferences.presentation.screens.preferences.PreferencesNavigator
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedulePreview.SchedulePreviewNavigator
import com.github.pwoicik.uekschedule.presentation.navigation.navigators.RootNavigator
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import timber.log.Timber

@Composable
fun RootNavHost() {
    val navController = rememberNavController()
    val currentDestination by navController.currentDestinationAsState()

    LaunchedEffect(currentDestination) {
        Timber.tag("root navGraph destination").d(currentDestination?.route)
    }

    DestinationsNavHost(
        navGraph = NavGraphs.root,
        navController = navController,
        dependenciesContainerBuilder = {
            val rootNavigator = remember(navController) {
                RootNavigator(navController)
            }
            dependency(rootNavigator as PreferencesNavigator)
            dependency(rootNavigator as SchedulePreviewNavigator)
        }
    )
}
