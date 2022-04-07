package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithError
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithLoading
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.NavGraphs
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.allGroups.AllGroupsScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.AllGroupsScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.ScheduleScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.YourGroupsScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main.components.MainScreenScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main.components.SnackbarVisualsWithPending
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main.components.SnackbarVisualsWithSuccess
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.navDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedule.ScheduleScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.yourGroups.YourGroupsScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.LocalBottomBarHeight
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.UpdateStatus
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.openPlayStorePage
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.updateApp
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigateTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun MainScreen(
    parentNavigator: DestinationsNavigator
) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState()
        .value?.navDestination

    SideEffect {
        Timber.tag("mainScreen navGraph destination")
            .d(currentDestination?.route.toString())
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        delay(2.seconds)
        context.updateApp().collectLatest { status ->
            when (status) {
                UpdateStatus.Canceled -> { snackbarHostState.currentSnackbarData?.dismiss() }
                is UpdateStatus.Downloading -> launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        visuals = SnackbarVisualsWithLoading(
                            message = context.resources.getString(R.string.updating_app),
                            progress = status.progress
                        )
                    )
                }
                UpdateStatus.Failed -> launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    val result = snackbarHostState.showSnackbar(
                        visuals = SnackbarVisualsWithError(
                            message = context.resources.getString(R.string.update_failed),
                            actionLabel = context.resources.getString(R.string.retry),
                            withDismissAction = true
                        )
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        context.openPlayStorePage()
                    }
                }
                UpdateStatus.Pending -> launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        visuals = SnackbarVisualsWithPending(
                            message = context.resources.getString(R.string.update_pending)
                        )
                    )
                }
                is UpdateStatus.Downloaded -> launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    val result = snackbarHostState.showSnackbar(
                        visuals = SnackbarVisualsWithSuccess(
                            message = context.resources.getString(R.string.update_ready),
                            actionLabel = context.resources.getString(R.string.update_install)
                        )
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        status.restartApp()
                    }
                }
            }
        }
    }

    CompositionLocalProvider(LocalBottomBarHeight provides 75.dp) {
        MainScreenScaffold(
            snackbarHostState = snackbarHostState,
            currentDestination = currentDestination,
            onDestinationClick = { destination ->
                navController.navigateTo(destination.direction) {
                    launchSingleTop = true
                    popUpTo(NavGraphs.mainScreen.startRoute.route)
                }
            }
        ) {
            DestinationsNavHost(
                navGraph = NavGraphs.mainScreen,
                navController = navController,
                modifier = Modifier.padding(WindowInsets.combinedBottomPaddingValues())
            ) {
                composable(ScheduleScreenDestination) {
                    ScheduleScreen(rootNavigator = parentNavigator)
                }
                composable(AllGroupsScreenDestination) {
                    AllGroupsScreen(rootNavigator = parentNavigator)
                }
                composable(YourGroupsScreenDestination) {
                    YourGroupsScreen(
                        navigator = destinationsNavigator,
                        rootNavigator = parentNavigator
                    )
                }
            }
        }
    }
}

@Composable
private fun WindowInsets.Companion.combinedBottomPaddingValues() = ime
    .union(navigationBars.add(WindowInsets(bottom = LocalBottomBarHeight.current)))
    .only(WindowInsetsSides.Bottom)
    .asPaddingValues()
