package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.yourGroups

import androidx.annotation.StringRes
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.OtherActivitiesScreenDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.SavedGroupsScreenDestination
import com.ramcosta.composedestinations.spec.Direction

enum class YourGroupsDestination(
    val direction: Direction,
    @StringRes val label: Int
) {

    SavedGroupsScreen(SavedGroupsScreenDestination, R.string.your_groups),
    OtherActivitiesScreen(OtherActivitiesScreenDestination, R.string.other_activities)
}
