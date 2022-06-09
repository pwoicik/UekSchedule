package com.github.pwoicik.uekschedule.presentation.navigation.screens.yourGroups

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.pwoicik.uekschedule.features.activities.presentation.screens.otherActivities.OtherActivitiesNavigator
import com.github.pwoicik.uekschedule.features.groups.presentation.screens.savedGroups.SavedGroupsNavigator
import com.github.pwoicik.uekschedule.presentation.navigation.NavGraphs
import com.github.pwoicik.uekschedule.presentation.navigation.navigators.YourGroupsNavigator
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
internal fun YourGroupsScreen(
    mainNavigator: DestinationsNavigator,
    rootNavigator: DestinationsNavigator
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry) {
        Timber.tag("yourGroups navGraph destination").d(currentBackStackEntry?.destination?.route)
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
            modifier = Modifier.padding(innerPadding),
            dependenciesContainerBuilder = {
                val yourGroupsNavigator =
                    remember(destinationsNavigator, mainNavigator, rootNavigator) {
                        YourGroupsNavigator(
                            yourGroupsNavController = destinationsNavigator,
                            mainNavController = mainNavigator,
                            rootNavController = rootNavigator
                        )
                    }
                dependency(yourGroupsNavigator as OtherActivitiesNavigator)
                dependency(yourGroupsNavigator as SavedGroupsNavigator)
            }
        )
    }
}
