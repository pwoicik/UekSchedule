package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.Constants
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithError
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithLoading
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithSuccess
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.NavGraphs
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.allGroups.AllGroupsScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.AllGroupsScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.SavedGroupsScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.ScheduleScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main.components.MainScreenScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main.components.SnackbarVisualsWithPending
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.navDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups.SavedGroupsScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedule.ScheduleScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.LocalBottomBarHeight
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.openPlayStorePage
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.updateApp
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavController
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigateTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalTime::class,
    ExperimentalMaterial3Api::class
)
@Destination(start = true)
@Composable
fun MainScreen(
    parentNavigator: DestinationsNavigator
) {
    val navController = rememberAnimatedNavController()
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
        updateApp(
            activity = context as AppCompatActivity,
            onUpdatePending = {
                launch {
                    if (snackbarHostState.currentSnackbarData?.visuals is SnackbarVisualsWithPending) return@launch
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        visuals = SnackbarVisualsWithPending(
                            message = context.resources.getString(R.string.update_pending)
                        )
                    )
                }
            },
            onUpdateDownloading = { progress ->
                launch {
                    if (
                        snackbarHostState.currentSnackbarData?.visuals is SnackbarVisualsWithLoading
                        && snackbarHostState.currentSnackbarData?.visuals !is SnackbarVisualsWithPending
                    ) return@launch
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        visuals = SnackbarVisualsWithLoading(
                            message = context.resources.getString(R.string.updating_app),
                            progress = progress
                        )
                    )
                }
            },
            onUpdateFailed = {
                launch {
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
            },
            onUpdateDownloaded = { restartApp ->
                launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    val result = snackbarHostState.showSnackbar(
                        visuals = SnackbarVisualsWithSuccess(
                            message = context.resources.getString(R.string.update_ready),
                            duration = SnackbarDuration.Indefinite,
                            actionLabel = context.resources.getString(R.string.update_install)
                        )
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        restartApp()
                    }
                }
            }
        )
    }

    CompositionLocalProvider(LocalBottomBarHeight provides Constants.BottomBarHeight) {
        MainScreenScaffold(
            snackbarHostState = snackbarHostState,
            currentDestination = currentDestination,
            onDestinationClick = { destination ->
                navController.popBackStack(NavGraphs.mainScreen.startRoute.route, inclusive = false)
                navController.navigateTo(destination.direction) {
                    launchSingleTop = true
                }
            }
        ) {
            DestinationsNavHost(
                navGraph = NavGraphs.mainScreen,
                navController = navController
            ) {
                composable(ScheduleScreenDestination) {
                    ScheduleScreen(parentNavigator = parentNavigator)
                }
                composable(SavedGroupsScreenDestination) {
                    SavedGroupsScreen(
                        parentNavigator = parentNavigator,
                        navigator = DestinationsNavController(
                            navController = navController,
                            navBackStackEntry = navBackStackEntry
                        )
                    )
                }
                composable(AllGroupsScreenDestination) {
                    AllGroupsScreen(parentNavigator = parentNavigator)
                }
            }
        }
    }
}
