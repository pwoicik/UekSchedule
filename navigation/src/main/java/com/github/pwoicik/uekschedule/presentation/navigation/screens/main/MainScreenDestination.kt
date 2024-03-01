package com.github.pwoicik.uekschedule.presentation.navigation.screens.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Groups
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.destinations.ScheduleScreenDestination
import com.github.pwoicik.uekschedule.features.search.presentation.screens.search.destinations.SearchScreenDestination
import com.github.pwoicik.uekschedule.presentation.navigation.screens.destinations.YourGroupsScreenDestination
import com.github.pwoicik.uekschedule.resources.R
import com.ramcosta.composedestinations.spec.Direction

@Suppress("UNUSED")
internal enum class MainScreenDestination(
    val direction: Direction,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    ScheduleScreen(ScheduleScreenDestination, Icons.Default.CalendarToday, R.string.schedule),
    YourGroupsScreen(YourGroupsScreenDestination, Icons.Default.Groups, R.string.your_groups),
    SearchScreen(SearchScreenDestination, Icons.AutoMirrored.Filled.ManageSearch, R.string.search)
}
