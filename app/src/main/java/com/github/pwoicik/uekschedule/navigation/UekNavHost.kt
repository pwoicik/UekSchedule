package com.github.pwoicik.uekschedule.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.github.pwoicik.uekschedule.screen.editGroups.EditGroupsScreen
import com.github.pwoicik.uekschedule.screen.scheduleScreen.ScheduleScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UekNavHost(
    navController: NavHostController,
    viewModel: ScheduleViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Schedule.route,
        modifier = modifier
    ) {
        composable(Routes.Schedule.route) {
            ScheduleScreen(
                viewModel = viewModel,
                onAddGroup = {
                    navController.navigate(Routes.EditGroups.route + "true")
                }
            )
        }

        composable(
            route = Routes.EditGroups.route + "{showPopup}",
            arguments = listOf(
                navArgument(name = "showPopup") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            EditGroupsScreen(
                viewModel = viewModel,
                showPopup = backStackEntry.arguments!!.getBoolean("showPopup")
            )
        }
    }
}
