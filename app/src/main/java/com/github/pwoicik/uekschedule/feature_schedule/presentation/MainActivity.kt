package com.github.pwoicik.uekschedule.feature_schedule.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.Screen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups.AddGroupsScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.classes.ClassesScreen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.manageGroups.ManageGroupsScreen
import com.github.pwoicik.uekschedule.ui.theme.UEKScheduleTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            UEKScheduleTheme {
                val navController = rememberNavController()
                val uiController = rememberSystemUiController()

                uiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = true
                )
                uiController.setNavigationBarColor(Color.Transparent)

                ProvideWindowInsets {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.ClassesScreen.route
                    ) {
                        composable(Screen.ClassesScreen.route) {
                            ClassesScreen(navController = navController)
                        }

                        composable(Screen.ManageGroupsScreen.route) {
                            ManageGroupsScreen(navController = navController)
                        }

                        composable(Screen.AddGroupsScreen.route) {
                            AddGroupsScreen(navController = navController)
                        }
                    }

                }
            }
        }
    }
}
