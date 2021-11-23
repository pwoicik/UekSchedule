package com.github.pwoicik.uekschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.github.pwoicik.uekschedule.database.ScheduleViewModelFactory
import com.github.pwoicik.uekschedule.navigation.Routes
import com.github.pwoicik.uekschedule.navigation.UekNavHost
import com.github.pwoicik.uekschedule.ui.theme.UEKScheduleTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: ScheduleViewModel = viewModel(
                factory = ScheduleViewModelFactory(application)
            )

            UekApp(viewModel)
        }
    }
}

@Composable
fun UekApp(viewModel: ScheduleViewModel) {
    val uiController = rememberSystemUiController()
    val navController = rememberNavController()

    UEKScheduleTheme {
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

@Composable
private fun UekTopAppBar(
    isExpanded: Boolean,
    toggleDropdown: () -> Unit,
    onDismiss: () -> Unit,
    onEditGroups: () -> Unit
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.app_name))
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        actions = {
            IconButton(
                onClick = toggleDropdown
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = onDismiss
                ) {
                    DropdownMenuItem(onClick = onEditGroups) {
                        Text(stringResource(R.string.edit_gropus))
                    }
                }
            }
        }
    )
}
