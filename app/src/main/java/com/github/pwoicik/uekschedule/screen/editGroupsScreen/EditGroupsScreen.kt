package com.github.pwoicik.uekschedule.screen.editGroupsScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
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

        AnimatedVisibility(
            visible = showPopup,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            AddGroupPopup(
                availableGroups = availableGroups,
                onDismiss = hidePopupFun,
                onConfirmAddGroup = viewModel::addSchedule
            )
        }
    }
}
