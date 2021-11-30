package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups.AddGroupsEvent
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups.AddGroupsViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding

@Composable
fun AddGroupsScaffold(
    scaffoldState: ScaffoldState,
    navController: NavController,
    viewModel: AddGroupsViewModel,
    selectedGroupsActionButtonsEnabled: Boolean,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = Modifier.navigationBarsWithImePadding(),
        backgroundColor = Color.Transparent,
        scaffoldState = scaffoldState,
        bottomBar = {
            AddGroupsBottomAppBar(
                onNavigateBack = {
                    navController.navigateUp()
                },
                selectedGroupsActionButtonsEnabled = selectedGroupsActionButtonsEnabled,
                onClearSelectedGroups = {
                    viewModel.event(AddGroupsEvent.ClearSelectedGroups)
                },
                onSaveSelectedGroups = {
                    viewModel.event(AddGroupsEvent.SaveSelectedGroups)
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}
