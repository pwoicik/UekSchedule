package com.github.pwoicik.uekschedule

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.github.pwoicik.uekschedule.components.UekTopAppBar
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.github.pwoicik.uekschedule.navigation.Routes
import com.github.pwoicik.uekschedule.navigation.UekNavHost
import com.github.pwoicik.uekschedule.ui.theme.UEKScheduleTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun UekApp(viewModel: ScheduleViewModel, systemInDarkTheme: Boolean) {
    val uiController = rememberSystemUiController()
    val navController = rememberNavController()

    var isDarkMode by remember { mutableStateOf(systemInDarkTheme) }

    UEKScheduleTheme(isDarkMode) {
        val barColor = MaterialTheme.colors.primary
        SideEffect {
            uiController.setStatusBarColor(
                color = barColor,
                darkIcons = false
            )
        }

        var dropdownIsExpanded by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                UekTopAppBar(
                    isExpanded = dropdownIsExpanded,
                    toggleDarkMode = {
                        isDarkMode = !isDarkMode
                    },
                    toggleDropdown = {
                        dropdownIsExpanded = !dropdownIsExpanded
                    },
                    onDismiss = {
                        dropdownIsExpanded = false
                    }
                ) {
                    dropdownIsExpanded = false
                    navController.navigate(Routes.EditGroups.route)
                }
            },
        ) { innerPadding ->

            UekNavHost(
                navController = navController,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
