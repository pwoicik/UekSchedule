package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addGroups.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addGroups.AddGroupsEvent
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addGroups.AddGroupsViewModel

@Composable
fun AddGroupsScaffold(
    scaffoldState: ScaffoldState,
    viewModel: AddGroupsViewModel,
    selectedGroupsActionButtonsEnabled: Boolean,
    onNavigateBack: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        backgroundColor = Color.Transparent,
        scaffoldState = scaffoldState,
        bottomBar = {
            AddGroupsBottomAppBar(
                onNavigateBack = onNavigateBack,
                selectedGroupsActionButtonsEnabled = selectedGroupsActionButtonsEnabled,
                onClearSelectedGroups = {
                    viewModel.event(AddGroupsEvent.ClearSelectedGroups)
                },
                onSaveSelectedGroups = {
                    viewModel.event(AddGroupsEvent.SaveSelectedGroups)
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            content = content
        )
    }
}
