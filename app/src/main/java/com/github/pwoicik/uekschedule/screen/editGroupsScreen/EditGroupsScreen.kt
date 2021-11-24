package com.github.pwoicik.uekschedule.screen.editGroupsScreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.github.pwoicik.uekschedule.screen.scheduleScreen.NoSavedGroups

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditGroupsScreen(viewModel: ScheduleViewModel, showPopup: Boolean) {
    @Suppress("NAME_SHADOWING")
    var showPopup by remember { mutableStateOf(showPopup) }

    val savedGroups by viewModel.groups.collectAsState()
    val availableGroups by viewModel.getAllAvailableGroups()

    val showPopupFun = { showPopup = true }
    val hidePopupFun = { showPopup = false }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (!savedGroups.isNullOrEmpty()) {
            GroupColumn(
                groups = savedGroups!!,
                onAddGroup = showPopupFun,
                onDeleteGroup = viewModel::deleteGroup
            )
        } else {
            NoSavedGroups(showPopupFun)
        }

        if (showPopup) {
            AddGroupPopup(
                availableGroups = availableGroups,
                onDismiss = hidePopupFun,
                onConfirmAddGroups = viewModel::addSchedules
            )
        }
    }
}
