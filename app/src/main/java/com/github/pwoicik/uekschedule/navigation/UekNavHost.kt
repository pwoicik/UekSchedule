package com.github.pwoicik.uekschedule.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
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
            ScheduleScreen(viewModel)
        }

        composable(Routes.EditGroups.route) {
            Text("TODO")
        }
    }
}
