package com.github.pwoicik.uekschedule

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.github.pwoicik.uekschedule.navigation.UekNavHost
import com.github.pwoicik.uekschedule.ui.theme.UEKScheduleTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun UekApp(viewModel: ScheduleViewModel, systemInDarkTheme: Boolean) {
    val uiController = rememberSystemUiController()
    val navController = rememberNavController()

    var isDarkMode by remember { mutableStateOf(systemInDarkTheme) }

    UEKScheduleTheme(isDarkMode) {
        val topBarColor = MaterialTheme.colors.primary
        val navBarColor = MaterialTheme.colors.surface
        SideEffect {
            uiController.setStatusBarColor(
                color = topBarColor,
                darkIcons = false
            )

            uiController.setNavigationBarColor(
                color = navBarColor,
                darkIcons = !isDarkMode
            )
        }

        UekNavHost(
            navController = navController,
            viewModel = viewModel,
            toggleDarkMode = {
                isDarkMode = !isDarkMode
            }
        )
    }
}
