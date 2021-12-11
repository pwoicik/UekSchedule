package com.github.pwoicik.uekschedule.feature_schedule.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.Screen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.AddActivityScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups.AddGroupsScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.manageActivities.ManageActivitiesScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.manageGroups.ManageGroupsScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule.ScheduleScreen
import com.github.pwoicik.uekschedule.ui.theme.UEKScheduleTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            UEKScheduleTheme {
                val navController = rememberNavController()
                val uiController = rememberSystemUiController()

                val isDarkMode = isSystemInDarkTheme()
                uiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkMode
                )

                ProvideWindowInsets {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.ScheduleScreen.route,
                        modifier = Modifier
                            .navigationBarsWithImePadding()
                    ) {
                        composable(Screen.ScheduleScreen.route) {
                            ScheduleScreen(navController = navController)
                        }

                        composable(Screen.ManageGroupsScreen.route) {
                            ManageGroupsScreen(navController = navController)
                        }

                        composable(Screen.AddGroupsScreen.route) {
                            AddGroupsScreen(navController = navController)
                        }

                        composable(Screen.ManageActivitiesScreen.route) {
                            ManageActivitiesScreen(navController = navController)
                        }

                        composable(
                            route = Screen.AddActivityScreen.route + "?activityId={activityId}",
                            arguments = listOf(
                                navArgument("activityId") {
                                    type = NavType.LongType
                                    defaultValue = -1L
                                }
                            )
                        ) {
                            AddActivityScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
