package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.AllGroupsDestinationDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.SavedGroupsDestinationDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.ScheduleDestinationDestination
import com.ramcosta.composedestinations.spec.Direction

enum class MainScreenDestination(
    val direction: Direction,
    val icon: ImageVector,
    @StringRes val label: Int
) {

    ScheduleScreen(ScheduleDestinationDestination, Icons.Default.CalendarToday, R.string.schedule),
    SavedGroupsScreen(SavedGroupsDestinationDestination, Icons.Default.Groups, R.string.your_groups),
    AllGroupsScreen(AllGroupsDestinationDestination, Icons.Default.ManageSearch, R.string.all_groups)
}
