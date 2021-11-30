package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.classes.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.Screen
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.classes.ClassesViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues

@Composable
fun ClassesScaffold(
    scaffoldState: ScaffoldState,
    isUpdating: Boolean,
    navController: NavController,
    viewModel: ClassesViewModel,
    content: @Composable () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars,
                    additionalStart = 8.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    enabled = !isUpdating,
                    onClick = {
                        navController.navigate(Screen.ManageGroupsScreen.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.manage_groups)
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = it) { snackbarData ->
                Snackbar(
                    action = {
                        IconButton(
                            enabled = !isUpdating,
                            onClick = viewModel::updateClasses
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.retry)
                            )
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(snackbarData.message)
                }
            }
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            content = content
        )
    }
}
