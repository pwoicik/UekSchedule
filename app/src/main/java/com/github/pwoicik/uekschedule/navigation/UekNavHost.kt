package com.github.pwoicik.uekschedule.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.github.pwoicik.uekschedule.screen.addGroupsScreen.AddGroupsScreen
import com.github.pwoicik.uekschedule.screen.savedGroupsScreen.SavedGroupsScreen
import com.github.pwoicik.uekschedule.screen.scheduleScreen.ScheduleScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UekNavHost(
    navController: NavHostController,
    viewModel: ScheduleViewModel,
    toggleDarkMode: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Schedule.route
    ) {
        val onEditGroups = {
            navController.navigate(Destinations.ManageGroups.route)
        }
        val onAddGroups = {
            navController.navigate(Destinations.AddGroups.route)
        }
        val goBack: () -> Unit = {
            navController.popBackStack()
        }

        composable(Destinations.Schedule.route) {
            ScheduleScreen(
                viewModel = viewModel,
                toggleDarkMode = toggleDarkMode,
                onEditGroups = onEditGroups,
                onAddGroups = onAddGroups
            )
        }

        navigation(
            route = Destinations.ManageGroups.route,
            startDestination = "saved-groups"
        ) {
            composable("saved-groups") {
                SavedGroupsScreen(
                    viewModel = viewModel,
                    onAddGroups = onAddGroups,
                    goBack = goBack,
                )
            }

            composable(Destinations.AddGroups.route) {
                AddGroupsScreen(
                    viewModel = viewModel,
                    goBack = goBack
                )
            }
        }
    }
}
